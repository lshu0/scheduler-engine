package com.sos.scheduler.engine.data.order;

import com.sos.scheduler.engine.base.sprayjson.JavaEnumJsonFormat;
import spray.json.JsonFormat;

/**
 * @author Joacim Zschimmer
 */
public enum OrderSourceType {
    adHoc,
    fileOrderSource,
    fileBased;

    public static final JsonFormat<OrderSourceType> MyJsonFormat = new JavaEnumJsonFormat<>(OrderSourceType.class);
}
