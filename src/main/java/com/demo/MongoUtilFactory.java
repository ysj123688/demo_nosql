package com.demo;

/**
 * MongoDB工具工厂类(支持多数据源)
 *
 * @author jerome_s@qq.com
 */
public class MongoUtilFactory {

	/**
	 * MongoDB数据源1
	 * 
	 * @author：suzhida
	 * @return
	 */
	public static MongoUtilI mongoUtil1() {
		return MongoUtil1.getInstance();
	}

	/**
	 * MongoDB数据源2
	 * 
	 * @author：suzhida
	 * @return
	 */
	public static MongoUtilI mongoUtil2() {
		return MongoUtil2.getInstance();
	}
}
