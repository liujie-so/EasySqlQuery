# EasySqlQuery
简单SQL查询
实现“关系型数据库”查询，主要是通过key-value对应到数据库column-value的形式实现。

例子：
IBasicDao dao = SpringBeanUtils.getBean("basicDao");
String table = "test";
Map<String, Object> params = create("age_gte", 18, "id", "1");
List<Map<String, Object>> list = null;
