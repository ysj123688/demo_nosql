package com.demo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bson.Document;

import com.mongodb.Bytes;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.UpdateOptions;

/**
 * MongoDB工具类(单数据源,单例模式)
 *
 * @author jerome_s@qq.com
 */
public class MongoUtil {

	public static MongoUtil mongoUtil = null;
	public static MongoClient mongoClient = null;
	public static MongoDatabase mongoDataBase = null;

	private MongoUtil() {

	}

	public static MongoUtil getInstance() {
		if (mongoUtil == null) {
			mongoUtil = new MongoUtil();
		}
		return mongoUtil;
	}

	/**
	 * 初始化
	 */
	public void init() {
		mongoUtil.closeMongoClient();
		mongoUtil.getMongoClient();
		mongoUtil.getMongoDataBase();
	}

	/**
	 * 关闭资源
	 */
	public void close() {
		mongoUtil.closeMongoClient();
	}

	/**
	 * 根据 collectionName 获取 collection
	 * 
	 * @param collectionName
	 * @return
	 */
	public static MongoCollection<Document> getCollection(String collectionName) {
		if (mongoDataBase == null) {
			return null;
		}
		return mongoDataBase.getCollection(collectionName);
	}

	private void getMongoClient() {
		try {
			String HOST = GetCfg.getValue("MongoDB_Host");
			int PORT = Integer.parseInt(GetCfg.getValue("MongoDB_Port"));
			String DATABASENAME = GetCfg.getValue("MongoDB_DBName");
			String USERNAME = GetCfg.getValue("MongoDB_UserName");
			char[] PASSWORD = GetCfg.getValue("MongoDB_PassWord").toCharArray();

			if (USERNAME != null && USERNAME.length() > 0) {
				// 需要权限认证的方式
				ServerAddress addr = new ServerAddress(HOST, PORT);
				MongoCredential credential = MongoCredential.createCredential(USERNAME, DATABASENAME, PASSWORD);
				mongoClient = new MongoClient(addr, Arrays.asList(credential));
				mongoClient.addOption(Bytes.QUERYOPTION_SLAVEOK);
			} else {
				// 无需权限认证的方式
				mongoClient = new MongoClient(HOST, PORT);
				mongoClient.addOption(Bytes.QUERYOPTION_SLAVEOK);
			}
			mongoDataBase = mongoClient.getDatabase(DATABASENAME);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void getMongoDataBase() {
		try {
			if (mongoClient != null) {
				String DATABASENAME = GetCfg.getValue("MongoDB_DBName");
				mongoDataBase = mongoClient.getDatabase(DATABASENAME);
			} else {
				throw new RuntimeException("MongoClient不能够为空");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void closeMongoClient() {
		if (mongoDataBase != null) {
			mongoDataBase = null;
		}
		if (mongoClient != null) {
			mongoClient.close();
		}
	}

	/**
	 * 插入一个文档
	 * 
	 * @param collectionName
	 *            集合名称
	 * @param document
	 *            文档
	 */
	public static void insert(String collectionName, Document document) {
		if (collectionName != null && !"".equals(collectionName) && document != null) {
			MongoCollection<Document> connection = MongoUtil.getCollection(collectionName);
			connection.insertOne(document);
		}
	}

	/**
	 * 插入多个文档
	 * 
	 * @param collectionName
	 *            集合名称
	 * @param documentList
	 *            文档列表
	 */
	public static void insertMany(String collectionName, List<Document> documentList) {
		if (collectionName != null && !"".equals(collectionName) && documentList != null && documentList.size() > 0) {
			MongoCollection<Document> connection = MongoUtil.getCollection(collectionName);
			connection.insertMany(documentList);
		}
	}

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
	public static List<Document> find(String collectionName, Document where, Document sort, int limit, int skip) {
		MongoCollection<Document> connection = MongoUtil.getCollection(collectionName);
		FindIterable<Document> iterable = connection.find(where).sort(sort).skip(skip).limit(limit);
		MongoCursor<Document> cursor = iterable.iterator();
		try {
			List<Document> list = new ArrayList<Document>();
			while (cursor.hasNext()) {
				Document doc = cursor.next();
				list.add(doc);
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			cursor.close();
		}
		return null;
	}

	/**
	 * 聚合查询
	 * 
	 * @param collectionName
	 *            集合名称
	 * @param docs
	 *            查询条件集合
	 * @return
	 */
	public static List<Document> findByAggregate(String collectionName, List<Document> docs) {
		MongoCollection<Document> connection = MongoUtil.getCollection(collectionName);
		AggregateIterable<Document> iterable = connection.aggregate(docs);
		MongoCursor<Document> cursor = iterable.iterator();
		try {
			List<Document> list = new ArrayList<Document>();
			while (cursor.hasNext()) {
				Document doc = cursor.next();
				list.add(doc);
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			cursor.close();
		}
		return null;
	}

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
	public static List<Document> findByAggregate(String collectionName, Document matchDoc, Document groupDoc) {
		List<Document> docs = new ArrayList<>();
		docs.add(matchDoc);
		docs.add(groupDoc);
		return findByAggregate(collectionName, docs);
	}

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
	public static List<Document> find(String collectionName, Document where, int limit, int skip) {
		return find(collectionName, where, null, limit, skip);
	}

	/**
	 * 根据条件查找文档个数
	 * 
	 * @param collectionName
	 *            集合名称
	 * @param where
	 *            条件
	 * @return
	 */
	public static Long count(String collectionName, Document where) {
		MongoCollection<Document> connection = MongoUtil.getCollection(collectionName);
		return connection.count(where);
	}

	/**
	 * 删除集合
	 * 
	 * @param collectionName
	 *            集合名称
	 */
	public static void dropCollection(String collectionName) {
		MongoCollection<Document> connection = MongoUtil.getCollection(collectionName);
		connection.drop();
	}

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
	public static void updateOrInsertOne(String collectionName, Document where, Document update) {
		if (collectionName != null && !"".equals(collectionName) && where != null && update != null) {
			MongoCollection<Document> connection = MongoUtil.getCollection(collectionName);
			UpdateOptions updateOptions = new UpdateOptions();
			updateOptions.upsert(true);// 集合不存在时创建一个集合
			connection.updateOne(where, new Document("$set", update), updateOptions);
		}
	}

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
	public static void updateOrInsertMany(String collectionName, Document where, Document update) {
		if (collectionName != null && !"".equals(collectionName) && where != null && update != null) {
			MongoCollection<Document> connection = MongoUtil.getCollection(collectionName);
			UpdateOptions updateOptions = new UpdateOptions();
			updateOptions.upsert(true);// 集合不存在时创建一个集合
			connection.updateMany(where, new Document("$set", update), updateOptions);
		}
	}

}
