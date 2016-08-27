package com.demo.mongodb;

import java.util.List;

import org.bson.Document;

/**
 * MongoDB工具 接口类
 * 
 * @author suzhida
 */
public interface MongoUtilI {

	/**
	 * 初始化
	 * 
	 * @author：suzhida
	 */
	void init();

	/**
	 * 关闭资源
	 * 
	 * @author：suzhida
	 */
	void close();

	void getMongoClient();

	void getMongoDataBase();

	void closeMongoClient();

	/**
	 * 插入一个文档
	 * 
	 * @param collectionName
	 *            集合名称
	 * @param document
	 *            文档
	 */
	void insert(String collectionName, Document document);

	/**
	 * 插入多个文档
	 * 
	 * @param collectionName
	 *            集合名称
	 * @param documentList
	 *            文档列表
	 */
	void insertMany(String collectionName, List<Document> documentList);

	/**
	 * 根据条件查找文档列表, 支持排序
	 * 
	 * @param collectionName
	 *            集合名称
	 * @param where
	 *            条件
	 * @param sort
	 *            排序
	 * @param limit
	 * @return
	 */
	List<Document> find(String collectionName, Document where, Document sort, int limit, int skip);

	/**
	 * 聚合查询
	 * 
	 * @param collectionName
	 *            集合名称
	 * @param docs
	 *            查询条件集合
	 * @return
	 */
	List<Document> findByAggregate(String collectionName, List<Document> docs);

	/**
	 * 聚合查询
	 * 
	 * @param collectionName
	 *            集合名称
	 * @param matchDoc
	 *            条件
	 * @param groupDoc
	 *            分组
	 * @return
	 */
	List<Document> findByAggregate(String collectionName, Document matchDoc, Document groupDoc);

	/**
	 * 根据条件查找文档列表
	 * 
	 * @param collectionName
	 *            集合名称
	 * @param where
	 *            条件
	 * @param limit
	 * @return
	 */
	List<Document> find(String collectionName, Document where, int limit, int skip);
	
	/**
	 * 根据条件查找文档个数
	 * 
	 * @param collectionName
	 *            集合名称
	 * @param where
	 *            条件
	 * @return
	 */
	Long count(String collectionName, Document where);
	
	/**
	 * 删除集合
	 * 
	 * @param collectionName
	 *            集合名称
	 */
	void dropCollection(String collectionName);
	
	/**
	 * 修改或者插入一个文档(集合不存在时，创建一个集合) 只修改一条记录
	 * 
	 * @param collectionName
	 *            集合名称
	 * @param where
	 *            条件
	 * @param update
	 *            更新文档
	 * @param options
	 */
	void updateOrInsertOne(String collectionName, Document where, Document update);
	
	/**
	 * 修改多个文档或者插入一个文档(集合不存在时，创建一个集合) 修改满足条件的所有数据
	 * 
	 * @param collectionName
	 *            集合名称
	 * @param where
	 *            条件
	 * @param update
	 *            更新文档
	 */
	void updateOrInsertMany(String collectionName, Document where, Document update);
}
