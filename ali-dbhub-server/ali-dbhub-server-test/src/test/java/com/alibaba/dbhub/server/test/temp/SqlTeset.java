package com.alibaba.dbhub.server.test.temp;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import com.alibaba.dbhub.server.domain.api.param.ConsoleConnectParam;
import com.alibaba.dbhub.server.domain.api.param.DataSourcePreConnectParam;
import com.alibaba.dbhub.server.domain.api.param.DlExecuteParam;
import com.alibaba.dbhub.server.domain.api.service.ConsoleService;
import com.alibaba.dbhub.server.domain.api.service.DataSourceService;
import com.alibaba.dbhub.server.domain.api.service.DlTemplateService;
import com.alibaba.dbhub.server.domain.api.service.TableService;
import com.alibaba.dbhub.server.domain.support.enums.DbTypeEnum;
import com.alibaba.dbhub.server.domain.support.model.ExecuteResult;
import com.alibaba.dbhub.server.domain.support.model.Sql;
import com.alibaba.dbhub.server.domain.api.param.SqlAnalyseParam;
import com.alibaba.dbhub.server.test.common.BaseTest;
import com.alibaba.dbhub.server.test.domain.data.service.dialect.MysqlDialectProperties;
import com.alibaba.dbhub.server.tools.base.wrapper.result.ActionResult;
import com.alibaba.dbhub.server.tools.base.wrapper.result.DataResult;
import com.alibaba.dbhub.server.tools.base.wrapper.result.ListResult;
import com.alibaba.fastjson2.JSON;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class SqlTeset extends BaseTest {

    @Resource
    private TableService tableService;
    @Resource
    private MysqlDialectProperties mysqlDialectProperties;
    @Resource
    private DataSourceService dataSourceService;
    @Resource
    private ConsoleService consoleService;
    @Resource
    private DlTemplateService dlTemplateService;

    @Test
    public void test() {
        // 创建
        DataSourcePreConnectParam dataSourceCreateParam = new DataSourcePreConnectParam();

        dataSourceCreateParam.setType(DbTypeEnum.MYSQL.getCode());
        dataSourceCreateParam.setUrl(mysqlDialectProperties.getUrl());
        dataSourceCreateParam.setUser(mysqlDialectProperties.getUsername());
        dataSourceCreateParam.setPassword(mysqlDialectProperties.getPassword());
        ActionResult actionResult = dataSourceService.preConnect(dataSourceCreateParam);

        DataResult<String> createTable = tableService.createTableExample(DbTypeEnum.MYSQL.getCode());
        log.info("sql1：{}", createTable.getData());
        SqlAnalyseParam sqlAnalyseParam = new SqlAnalyseParam();
        sqlAnalyseParam.setDataSourceId(1L);
        sqlAnalyseParam.setSql(createTable.getData());
        List<Sql> sqlList = new ArrayList<>();
        sqlList.add(Sql.builder().sql(createTable.getData()).build());

        // 创建控制台
        ConsoleConnectParam consoleCreateParam = new ConsoleConnectParam();
        consoleCreateParam.setDataSourceId(1L);
        consoleCreateParam.setConsoleId(1L);
        consoleCreateParam.setDatabaseName(mysqlDialectProperties.getDatabaseName());
        consoleService.createConsole(consoleCreateParam);

        // 删除
        DlExecuteParam templateQueryParam = new DlExecuteParam();
        templateQueryParam.setConsoleId(1L);
        templateQueryParam.setDataSourceId(1L);
        templateQueryParam.setSql("drop table test;");
        ListResult<ExecuteResult> executeResult = dlTemplateService.execute(templateQueryParam);
        log.info("result:{}", JSON.toJSONString(executeResult));

        // 创建表结构
        templateQueryParam = new DlExecuteParam();
        templateQueryParam.setConsoleId(1L);
        templateQueryParam.setDataSourceId(1L);
        templateQueryParam.setSql(sqlList.get(0).getSql());
        executeResult = dlTemplateService.execute(templateQueryParam);
        log.info("result:{}", JSON.toJSONString(executeResult));
    }
}
