# excel-util

`特殊说明：jar包请在附件下载`
`https://gitee.com/xwzhang1/excel-util/attach_files`

#### 介绍

通过反射机制实现的支持较全自定义配置的Excel表格导入的工具。

当导入本工具jar包后，可以实现以下功能：

```
1. 实现大部分导入的业务需求，表格中包含核心数据以及特别说明数据。
2. 提供表格单元格数据格式转换校验，提供`ExcelValidator`类结合`validation-api`可以实现判空、字符长度等`validation-api`支持的校验
3. 当导入数据未通过校验时，在原表格的标题行最后会添加`错误信息`列，并在存在错误信息的行表示出红色的错误信息。
4. 支持表格数据一错全错或者仅返回错误数据的提示，正确数据提供给用户操作等自定义配置。详细配置见`ExcelImportObject`类。
5. 对于表中的单个数据，提供单元格数据获取的方法，获取对应单元格数据，不过需要开发者单独对其进行业务验证
6. 通过源码demo可以很快了解该工具的使用方法。
```

#### 包描述

`com.poi.exceldeal.cotroller ` 作为本web项目的入口和出口

`com.poi.exceldeal.facade `用于执行接收表格数据的实体类，需继承ExcelModelRequest

`com.poi.exceldeal.util `工具类逻辑在这里边编写，也是本项目的核心包

`com.poi.exceldeal.webservice`本demo的业务逻辑层，需求处理逻辑将在该层实现

#### 安装教程

1. 下载源码，可自行修改使用
2. 下载并导入jar包，使用已经封装的方法(本jar包会在之后更新之后上传到本git地址的附件模块)
3. 本demo使用spring-boot 2.1.1.RELEASE版本，Java 1.8。其他版本请自测

#### 导入使用说明

注意: 文件以byte[]的格式作为参数提供给工具，具体方式请查看demo中的DealExcelController实例。此处说明仅针对核心数据的操作进行介绍，特殊数据，请使用ExcelUtil.getObjectByRowAndCell([文件流的byte流],[行序号],[列序号],[类型类,如：String.class])

1. 抽取出需要进行操作的表格中的核心数据，根据标题创建对应的实体类并继承ExcelImportRequest类（属性的顺序必须与表格中的顺序保持一致，且不可有多余属性，即不可在此定义序列号），并对相关字段使用validation-api注解添加注解。

   ```java
   public class ExcelModel extends ExcelModelRequest {
   
       @NotBlank(message = "姓名不能为空，")
       @Size(min = 3, max = 5, message = "姓名个数必须在3到5个字之间，")
       private String name;
   
       @NotNull(message = "年龄不能为空，")
       @Max(value = 100, message = "年龄不能超过100，")
       private Integer age;
   }
   ```

2. 构造ExcelImportObject类，具体构造方式请查看demo中的ExcelWebService类

   ```java
    ExcelImportObject<ExcelModel> rule = new ExcelImportObject<>();
           rule.setFileInputStream(uploadFile);
           rule.setFirstRowNum(2);
           rule.setVerifyDuplicationCells(new Integer[]{1});
   ```

3. 将自定义的实体类与构造好的ExcelImportObject对象作为参数调用ExcelUtil.exchangeExcelStreamToList方法，该方法会将操作得到的信息写入参数ExcelImportObject对象中并返回该对象。错误信息将以Map<[错误行数(以被操作的第一行为1计数)],[错误信息]>的形式进行存储，对应字段名为errorMap

   ```java
   rule = ExcelUtil.exchangeExcelStreamToList(ExcelModel.class, rule);
   ```

4. 使用ExcelValidator类对转换得到的ExcelImportObject继续进行验证，你只需要把上一步得到的结果往提供的方法里边丢就行了

   ```java
   rule = ExcelValidator.excelValidate(rule);
   ```

5. 进行逻辑验证，即根据产品业务需求进行的验证（如：数据库重复验证等），验证对象将封装在rule.getResultList()中，验证时请按照以下标准，errorMessageDeal方法接收的参数为([ExcelImportObject对象],[出错的List标号],[错误信息])，该方法会自动将错误信息视情况添加或者新增到ExcelImportObject对象的errorMap中。

   ```java
   for (int i = 0; i < rule.getResultList().size(); i++) {
               StringBuffer errorMessage = new StringBuffer();
               // 处理自己的逻辑并将错误信息写入errorMessage
               ExcelUtil.errorMessageDeal(rule, i, errorMessage);
           }
   ```

6. writeErrorMessageToExcel方法会将以上步骤得到的错误数据根据errorMap中的数据格式写回到表格中并返回一个byte[]类型的对象供开发人员操作。

   ```
   byte[] result = ExcelUtil.writeErrorMessageToExcel(rule);
   ```

在第5步执行结束后，将会得到我们需要得到的所有数据，包含错误数据，正确数据，等。我们只需要根据自己的需要，从ExcelImportObject对象中获取对应数据进行操作即可。

#### 导出使用说明

1. 根据导出模板创建顺序对应的实体类，需要导出的数据封装到该实体类中，且该实体类继承类ExcelExportRequest是必须的。

2. 在实体类中各个属性上添加注解@CellTitleValue(value =  "标题")，导出的表格中的每一列的标题将使用该注解value属性的值。

3. 配置ExcelExportObject类，作为导出的必要配置条件。`exportList`参数为欲导出的List对象。

   ```
   ExcelExportObject<ExcelExportModel> orderExport = new ExcelExportObject<>();
           orderExport.setTableName("XXXX导出数据");
           orderExport.setExportList(exportList);
   ```

4. 调用导出方法，该方法会返回一个表格文件转换得到的byte[]对象，之后的操作自行实现。

   ```
   ExcelUtil.excelExport(ExcelExportModel.class, orderExport)
   ```

5. 完事了！  特别说明：导出暂时没有设置特殊单元格的值设置，默认只导出核心数据。

#### 参与贡献

1. Fork 本仓库
2. 新建 自己的 分支
3. 提交代码
4. 新建 Pull Request

#### 特别说明

由于本人在技术领域研究的时间并不多，想的不是很全面，所以很多地方的实现显得不是那么的完美，如果有更好的建议，还请在Issues指示，工具实现过程以及原理并不难以理解。但是如果你在使用本工具过程中有任何问题都可以在Issues中提出，我看到了一定会耐心答复。