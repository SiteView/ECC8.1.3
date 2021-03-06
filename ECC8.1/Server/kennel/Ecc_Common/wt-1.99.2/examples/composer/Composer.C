/*
 * Copyright (C) 2006 Koen Deforche, Kessel-Lo, Belgium.
 *
 * See the LICENSE file for terms of use.
 */
#include <iostream>

#include "AddresseeEdit.h"
#include "AttachmentEdit.h"
#include "Composer.h"
#include "ContactSuggestions.h"
#include "Label.h"
#include "Option.h"
#include "OptionList.h"

#include <WContainerWidget>
#include <WImage>
#include <WLineEdit>
#include <WPushButton>
#include <WText>
#include <WTable>
#include <WTableCell>
#include <WStringUtil>

Composer::Composer(WContainerWidget *parent)
  : WCompositeWidget(parent),
    saving_(false),
    sending_(false)
{
  setImplementation(layout_ = new WContainerWidget());

  createUi();
}

void Composer::setTo(const std::vector<Contact>& to)
{
  toEdit_->setAddressees(to);
}

void Composer::setSubject(const std::wstring subject)
{
  subject_->setText(subject);
}

void Composer::setMessage(const std::wstring message)
{
  message_->setText(message);
}

std::vector<Contact> Composer::to() const
{
  return toEdit_->addressees();
}

std::vector<Contact> Composer::cc() const
{
  return ccEdit_->addressees();
}
 
std::vector<Contact> Composer::bcc() const
{
  return bccEdit_->addressees();
}

void Composer::setAddressBook(const std::vector<Contact>& contacts)
{
  contactSuggestions_->setAddressBook(contacts);
}

std::wstring Composer::subject() const
{
  return subject_->text();
}

std::vector<Attachment> Composer::attachments() const
{
  std::vector<Attachment> attachments;

  for (unsigned i = 0; i < attachments_.size() - 1; ++i) {
    if (attachments_[i]->include())
      attachments.push_back(attachments_[i]->attachment());
  }

  return attachments;
}

std::wstring Composer::message() const
{
  return message_->text();
}

void Composer::createUi()
{
  setStyleClass(L"darker");

  // horizontal layout container, used for top and bottom buttons.
  WContainerWidget *horiz;

  /*
   * Top buttons
   */
  horiz = new WContainerWidget(layout_);
  horiz->setPadding(WLength(5));
  topSendButton_ = new WPushButton(tr("msg.send"), horiz);
  topSendButton_->setStyleClass(L"default"); // default action
  topSaveNowButton_ = new WPushButton(tr("msg.savenow"), horiz);
  topDiscardButton_ = new WPushButton(tr("msg.discard"), horiz);

  // Text widget which shows status messages, next to the top buttons.
  statusMsg_ = new WText(horiz);
  statusMsg_->setMargin(WLength(15, WLength::Pixel), Left);

  /*
   * To, Cc, Bcc, Subject, Attachments
   *
   * They are organized in a two-column table: left column for
   * labels, and right column for the edit.
   */
  edits_ = new WTable(layout_);
  edits_->setStyleClass(L"lighter");
  edits_->resize(WLength(100, WLength::Percentage), WLength());
  edits_->elementAt(0, 0)->resize(WLength(1, WLength::Percentage), WLength());

  /*
   * To, Cc, Bcc
   */
  toEdit_ = new AddresseeEdit(tr("msg.to"), edits_->elementAt(0, 1),
			      edits_->elementAt(0, 0));
  // add some space above To:
  edits_->elementAt(0, 1)->setMargin(WLength(5), Top);
  ccEdit_ = new AddresseeEdit(tr("msg.cc"), edits_->elementAt(1, 1),
			      edits_->elementAt(1, 0));
  bccEdit_ = new AddresseeEdit(tr("msg.bcc"), edits_->elementAt(2, 1),
			       edits_->elementAt(2, 0));

  ccEdit_->hide();
  bccEdit_->hide();

  /*
   * Addressbook suggestions popup
   */
  contactSuggestions_ = new ContactSuggestions(layout_);
  contactSuggestions_->setStyleClass(L"suggest");

  contactSuggestions_->forEdit(toEdit_);
  contactSuggestions_->forEdit(ccEdit_);
  contactSuggestions_->forEdit(bccEdit_);

  /*
   * We use an OptionList widget to show the expand options for
   * ccEdit_ and bccEdit_ nicely next to each other, separated
   * by pipe characters.
   */
  options_ = new OptionList(edits_->elementAt(3, 1));

  options_->add(addcc_ = new Option(tr("msg.addcc")));
  options_->add(addbcc_ = new Option(tr("msg.addbcc")));

  /*
   * Subject
   */
  new Label(tr("msg.subject"), edits_->elementAt(4, 0));
  subject_ = new WLineEdit(edits_->elementAt(4, 1));
  subject_->resize(WLength(99, WLength::Percentage), WLength());

  /*
   * Attachments
   */
  new WImage("paperclip.png", edits_->elementAt(5, 0));
  edits_->elementAt(5, 0)->setContentAlignment(AlignRight | AlignTop);

  
  // Attachment edits: we always have the next attachmentedit ready
  // but hidden. This improves the response time, since the show()
  // and hide() slots are stateless.
  attachments_.push_back(new AttachmentEdit(this, edits_->elementAt(5, 1)));
  attachments_.back()->hide();

  /*
   * Two options for attaching files. The first does not say 'another'.
   */
  attachFile_ = new Option(tr("msg.attachfile"),
			   edits_->elementAt(5, 1));
  attachOtherFile_ = new Option(tr("msg.attachanother"),
				edits_->elementAt(5, 1));
  attachOtherFile_->hide();

  /*
   * Message
   */
  message_ = new WTextArea(layout_);
  message_->setColumns(80);
  message_->setRows(10); // should be 20, but let's keep it smaller
  message_->setMargin(WLength(10, WLength::Pixel));

  /*
   * Bottom buttons
   */
  horiz = new WContainerWidget(layout_);
  horiz->setPadding(WLength(5));
  botSendButton_ = new WPushButton(tr("msg.send"), horiz);
  botSendButton_->setStyleClass(L"default");
  botSaveNowButton_ = new WPushButton(tr("msg.savenow"), horiz);
  botDiscardButton_ = new WPushButton(tr("msg.discard"), horiz);

  /*
   * Button events.
   */
  topSendButton_->clicked.connect(SLOT(this, Composer::sendIt));
  botSendButton_->clicked.connect(SLOT(this, Composer::sendIt));
  topSaveNowButton_->clicked.connect(SLOT(this, Composer::saveNow));
  botSaveNowButton_->clicked.connect(SLOT(this, Composer::saveNow));
  topDiscardButton_->clicked.connect(SLOT(this, Composer::discardIt));
  botDiscardButton_->clicked.connect(SLOT(this, Composer::discardIt));

  /*
   * Option events to show the cc or Bcc edit.
   *
   * Clicking on the option should both show the corresponding edit, and
   * hide the option itself.
   */
  addcc_->clicked.connect(SLOT(ccEdit_, WWidget::show));
  addcc_->clicked.connect(SLOT(addcc_, WWidget::hide));
  addcc_->clicked.connect(SLOT(options_, OptionList::update));

  addbcc_->clicked.connect(SLOT(bccEdit_, WWidget::show));
  addbcc_->clicked.connect(SLOT(addbcc_, WWidget::hide));
  addbcc_->clicked.connect(SLOT(options_, OptionList::update));

  /*
   * Option event to attach the first attachment.
   *
   * We show the first attachment, and call attachMore() to prepare the
   * next attachment edit that will be hidden.
   *
   * In addition, we need to show the 'attach More' option, and hide the
   * 'attach' option.
   */
  attachFile_->clicked.connect(SLOT(attachments_.back(), WWidget::show));
  attachFile_->clicked.connect(SLOT(attachOtherFile_, WWidget::show));
  attachFile_->clicked.connect(SLOT(attachFile_, WWidget::hide));
  attachFile_->clicked.connect(SLOT(this, Composer::attachMore));
  attachOtherFile_->clicked.connect(SLOT(this, Composer::attachMore));
}

void Composer::attachMore()
{
  /*
   * Create and append the next AttachmentEdit, that will be hidden.
   */
  AttachmentEdit *edit = new AttachmentEdit(this);
  edits_->elementAt(5, 1)->insertWidget(edit, attachOtherFile_);
  attachments_.push_back(edit);
  attachments_.back()->hide();

  // Connect the attachOtherFile_ option to show this attachment.
  attachOtherFile_->clicked.connect(SLOT(attachments_.back(), WWidget::show));
}

void Composer::removeAttachment(AttachmentEdit *attachment)
{
  /*
   * Remove the given attachment from the attachments list.
   */
  std::vector<AttachmentEdit *>::iterator i
    = std::find(attachments_.begin(), attachments_.end(), attachment);

  if (i != attachments_.end()) {
    attachments_.erase(i);
    delete attachment;

    if (attachments_.size() == 1) {
      /*
       * This was the last visible attachment, thus, we should switch
       * the option control again.
       */
      attachOtherFile_->hide();
      attachFile_->show();
      attachFile_->clicked.connect(SLOT(attachments_.back(), WWidget::show));
    }
  }
}

void Composer::sendIt()
{
  if (!sending_) {
    sending_ = true;

    /*
     * First save -- this will check for the sending_ state
     * signal if successfull.
     */
    saveNow();
  }
}

void Composer::saveNow()
{
  if (!saving_) {
    saving_ = true;

    /*
     * Check if any attachments still need to be uploaded.
     * This may be the case when fileupload change events could not
     * be caught (for example in Konqueror).
     */
    attachmentsPending_ = 0;

    for (unsigned i = 0; i < attachments_.size() - 1; ++i) {
      if (attachments_[i]->uploadNow()) {
	++attachmentsPending_;

	// this will trigger attachmentDone() when done, see
	// the AttachmentEdit constructor.
      }
    }

    std::cerr << "Attachments pending: " << attachmentsPending_ << std::endl;
    if (attachmentsPending_)
      setStatus(tr("msg.uploading"), L"status");
    else
      saved();
  }
}

void Composer::attachmentDone()
{
  if (saving_) {
    --attachmentsPending_;
    std::cerr << "Attachments still: " << attachmentsPending_ << std::endl;

    if (attachmentsPending_ == 0)
      saved();
  }
}

void Composer::setStatus(const WMessage& text, const wchar_t *style)
{
  statusMsg_->setText(text);
  statusMsg_->setStyleClass(style);
}

void Composer::saved()
{
  /*
   * All attachments have been processed.
   */

  bool attachmentsFailed = false;
  for (unsigned i = 0; i < attachments_.size() - 1; ++i)
    if (attachments_[i]->uploadFailed()) {
      attachmentsFailed = true;
      break;
    }

  if (attachmentsFailed) {
    setStatus(tr("msg.attachment.failed"), L"error");
  } else {
#ifndef WIN32
    time_t t = time(0);
    struct tm td;
    gmtime_r(&t, &td);
    char buffer[100];
    strftime(buffer, 100, "%H:%M", &td);
#else
    char buffer[] = "server"; // Should fix this; for now just make sense
#endif
    setStatus(tr("msg.ok"), L"status");
    statusMsg_->setText(std::wstring(L"Draft saved at ") + widen(buffer));

    if (sending_) {
      send.emit();
      return;
    }
  }

  saving_ = false;
  sending_ = false;
}

void Composer::discardIt()
{ 
  discard.emit();
}
