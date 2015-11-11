/*
 * Copyright (C) 2009 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.wicare.wistorm.ui.wzxing;

import com.google.zxing.ResultPoint;
import com.google.zxing.ResultPointCallback;

public final class WZxingViewfinderResultPointCallback implements ResultPointCallback {

  private final WZxingViewfinderView wZxingViewfinderView;

  public WZxingViewfinderResultPointCallback(WZxingViewfinderView wZxingViewfinderView) {
    this.wZxingViewfinderView = wZxingViewfinderView;
  }

  public void foundPossibleResultPoint(ResultPoint point) {
    wZxingViewfinderView.addPossibleResultPoint(point);
  }

}
