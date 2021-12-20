package com.decard.androidtest.utils;

import android.util.Log;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Time:2021/12/19
 * Author:SunnyLuna
 * 反射的操作都是编译后的操作
 * 编译之后集合的泛型是去泛型化的
 * java中集合的泛型，是防止错误输入的，只在编译阶段有效
 */
public class ReflectUtils {

	private static final String TAG = "---ReflectUtils";

	/*
	 类是java.lang.Class的实例对象
	 不仅表示类的类类型， 还代表了动态加载类
	 编译时加载类是静态加载类，运行时加载类是动态加载类

	 */

	public static void main(String[] args) {
		String s = "fdfdf";
		printClassInfo(s);
	}

	public static void printClassInfo(Object object) {
		//获取类的信息，首先获取类的类类型
		Class c = object.getClass();
		//获取类的名称
		Log.d(TAG, "printClassInfo: " + c.getName());
		Log.d(TAG, "printClassInfo: " + c.getSimpleName());
		//一个成员方法就是一个method对象
		//getMethods()获取的是所有public的函数，包括父类继承而来的
		//getDeclaredMethods获取的是所有该类声明的方法，不问访问权限
		Method[] ms = c.getMethods();
		//Method[] ms=c.getDeclaredMethods();
		for (int i = 0; i < ms.length; i++) {
			//获取方法的返回值类型的类类型
			Class returnType = ms[i].getReturnType();
			Log.d(TAG, "printClassInfo: returnType" + returnType.getName());
			//得到方法的名称
			Log.d(TAG, "printClassInfo: 方法的名称" + ms[i].getName());
			//获取参数类型，得到的是参数列表的类类型
			Class<?>[] parameterTypes = ms[i].getParameterTypes();
			for (Class parameterType : parameterTypes) {

				Log.d(TAG, "printClassInfo: " + parameterType.getName());
			}
		}

		/*
		 成员变量也是对象
		 java.lang.reflect.Field
		 Field类封装了关于成员变量的操作
		 getField()获取的是所有public的成员变量信息
         getDeclaredFields()获取的是该类自己声明的成员变量信息
		 */
		Field[] fields = c.getFields();
		for (Field field : fields) {
			//得到成员变量的类型的类类型
			Class<?> fieldType = field.getType();
			String fieldTypeName = fieldType.getName();
			//成员变量的名称
			String fieldName = field.getName();
			Log.d(TAG, "printClassInfo: " + fieldTypeName + fieldName);
		}


     	/*
		 构造函数也是对象
		 java.lang.reflect.Constructor
		 Field类封装了关于构造函数的信息
		 getConstructors 获取所有public的构造函数
		 getDeclaredConstructors得到所有的构造函数
		 */
		//		Constructor[] constructors = c.getConstructors();
		Constructor[] constructors = c.getDeclaredConstructors();

		for (Constructor constructor : constructors) {
			//获取构造函数的类类型
			Class[] constructorParameterTypes = constructor.getParameterTypes();
			for (Class class1 : constructorParameterTypes) {
				Log.d(TAG, "printClassInfo: " + class1.getName());
			}
		}
	}

	/**
	 * 获取一个方法就是获取类的类信息，获取类信息首先要获取类类型
	 * 获取方法，名称和参数类型来决定
	 * getMethod()获取的是public的方法
	 * getDeclaredMethod()获取自己声明的方法
	 */

	public static void getMethodInfo(Object object){
		Class c = object.getClass();
//		c.getMethod("ni",new Class[]{int.class,int.class});
		//无参
//		c.getMethod("ni",new Class[]{});
//		c.getMethod("ni");
		try {
			Method m = c.getMethod("print", int.class, int.class);
			//方法的反射操作
			//object.print(10,20)
			//方法的反射操作是用m对象来进行方法调用，和object.print调用效果一样
			//没有返回值返回null,有返回对应的返回值
//			Object invoke = m.invoke(object, new Object[]{});
			Object invoke = m.invoke(object, 10, 10);


		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}
}
