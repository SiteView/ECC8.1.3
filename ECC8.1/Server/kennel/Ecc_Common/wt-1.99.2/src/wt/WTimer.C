/*
 * Copyright (C) 2006 Koen Deforche, Kessel-Lo, Belgium.
 *
 * See the LICENSE file for terms of use.
 */

#include "WApplication"
#include "WTimer"
#include "WTimerWidget"
#include "WContainerWidget"
#include "TimeUtil.h"

namespace Wt {

WTimer::WTimer(WObject *parent)
  : WObject(parent),
    timerWidget_(new WTimerWidget(this)),
    timeout(&timerWidget_->clicked),
    singleShot_(false),
    selfDestruct_(false),
    interval_(0),
    active_(false),
    timeout_(new Time())
{
  timeout.connect(SLOT(this, WTimer::gotTimeout));
}

WTimer::~WTimer()
{
  if (active_)
    stop();

  delete timerWidget_;
}

void WTimer::setInterval(int msec)
{
  interval_ = msec;
}

void WTimer::setSingleShot(bool singleShot)
{
  singleShot_ = singleShot;
}

void WTimer::start()
{
  if (!active_) {
    if (WApplication::instance())
      WApplication::instance()->timerRoot()->addWidget(timerWidget_);
    active_ = true;
    *timeout_ = Time() + interval_;
    timerWidget_->timerStart();
  }
}

void WTimer::stop()
{
  if (active_) {
    if (WApplication::instance() && timerWidget_)
      WApplication::instance()->timerRoot()->removeWidget(timerWidget_);
    active_ = false;
  }
}

void WTimer::setSelfDestruct()
{
  selfDestruct_ = true;
}

void WTimer::gotTimeout()
{
  if (!singleShot_) {
    *timeout_ = Time() + interval_;
    timerWidget_->timerStart();    
  } else
    stop();

  if (selfDestruct_)
    delete this;
}

int WTimer::getRemainingInterval() const
{
  int remaining = *timeout_ - Time();
  return std::max(0, remaining);
}

}
