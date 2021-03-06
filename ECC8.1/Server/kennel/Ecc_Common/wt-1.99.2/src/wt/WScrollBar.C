/*
 * Copyright (C) 2005 Koen Deforche, Kessel-Lo, Belgium.
 *
 * See the LICENSE file for terms of use.
 */

#include "WScrollBar"
#include "WScrollArea"
#include "DomElement.h"

using std::find;

namespace Wt {

WScrollBar::WScrollBar(WScrollArea *area, Orientation orientation)
  : scrollArea_(area),
    orientation_(orientation),
    tiesChanged_(false),
    valueSet_(false)
{ }

WScrollBar::~WScrollBar()
{
  while (ties_.size())
    unTie(this, ties_[0]);
}

void WScrollBar::setValue(int value)
{
  value_ = value;
  valueSet_ = true;

  scrollArea_->scrollBarChanged();
}

void WScrollBar::tie(WScrollBar *one, WScrollBar *two)
{
  one->ties_.push_back(two);
  two->ties_.push_back(one);

  one->tiesChanged_ = true;
  two->tiesChanged_ = true;
  one->scrollArea_->scrollBarChanged();
  two->scrollArea_->scrollBarChanged();
}

void WScrollBar::unTie(WScrollBar *one, WScrollBar *two)
{
  one->ties_.erase(find(one->ties_.begin(),
			one->ties_.end(), two));
  two->ties_.erase(find(two->ties_.begin(),
			two->ties_.end(), one));

  one->tiesChanged_ = true;
  two->tiesChanged_ = true;
  one->scrollArea_->scrollBarChanged();
  two->scrollArea_->scrollBarChanged();
}

void WScrollBar::updateDom(DomElement& element, bool all)
{
  if (tiesChanged_ || all) {
    std::string jsCode;
    for (unsigned i = 0; i < ties_.size(); ++i) {
      DomElement *tieElement = DomElement::getForUpdate(ties_[i]->scrollArea_,
							DomElement::DIV);
      DomElement *scrollElement = DomElement::getForUpdate(scrollArea_,
							   DomElement::DIV);

      jsCode +=
	tieElement->createReference() + ".scroll"
	+ (orientation_ == Horizontal ? "Left" : "Top")
	+ "=" + scrollElement->createReference() + ".scroll"
	+ (orientation_ == Horizontal ? "Left" : "Top") + ";";

      delete tieElement;
      delete scrollElement;
    }

    element.setEvent("scroll", jsCode, "");

    tiesChanged_ = false;
  }
}

/*
  for iframe (but does not work either in konqueror)

  window.pageXOffset
  window.pageYOffset
  document.body.scrollLeft
  document.body.scrollTop

  window.scrollTo()
*/

}
