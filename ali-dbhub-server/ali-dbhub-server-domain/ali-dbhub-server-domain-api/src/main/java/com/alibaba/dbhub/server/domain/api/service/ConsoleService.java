package com.alibaba.dbhub.server.domain.api.service;

import com.alibaba.dbhub.server.domain.api.param.ConsoleConnectParam;
import com.alibaba.dbhub.server.domain.api.param.ConsoleCloseParam;
import com.alibaba.dbhub.server.tools.base.wrapper.result.ActionResult;

/**
 * 数据源管理服务
 *
 * @author moji
 * @version DataSourceCoreService.java, v 0.1 2022年09月23日 15:22 moji Exp $
 * @date 2022/09/23
 */
public interface ConsoleService {

    /**
     * 创建console链接
     *
     * @param param
     * @return
     */
    ActionResult createConsole(ConsoleConnectParam param);

    /**
     * 关闭连接
     *
     * @param param
     * @return
     */
    ActionResult closeConsole(ConsoleCloseParam param);

}
