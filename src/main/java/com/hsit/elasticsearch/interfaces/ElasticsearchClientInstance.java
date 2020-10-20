package com.hsit.elasticsearch.interfaces;


import com.hsit.elasticsearch.common.ELKTools;

import java.io.IOException;

public interface ElasticsearchClientInstance {
    /**
     * 关闭连接接口
     */
    void closeAndDestroy() throws IOException;

    /**
     * 判断是否为空
     *
     * @param objects
     * @return boolean
     */
    default boolean isEmpty(Object... objects) {
        return ELKTools.isEmpty(objects);
    }

}
