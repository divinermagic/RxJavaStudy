package com.magic.vulcan.rxjavastudy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.magic.vulcan.rxjavastudy.bean.Author;
import com.magic.vulcan.rxjavastudy.bean.DataUtils;
import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;

import rx.Observable;
import rx.Observer;
import rx.Scheduler;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * RxJava的基础学习
 */
public class MainActivity extends AppCompatActivity {

    private static final String TAG_RxJava = "RxJava";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //初始化Logger
        Logger.init(TAG_RxJava).logLevel(LogLevel.FULL);
        // create01();
        // from02();
        // just03();
        // subscribe04();
        // map05();
        // flatMap07();
        scheduler08();
    }

    /**
     * RxJava 使用 Observable.create() 方法来创建一个 Observable ，并为它定义事件触发规则：
     */
    private void create01() {
        /*Observable:被观察者[类似Button]*/
        Observable<String> observable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("Hello World");
                subscriber.onNext("Hello JiKeXueYuan");
                subscriber.onCompleted();
            }
        });
        /*Observer:观察者[类似Button的点击事件]*/
        Observer<String> observer = new Observer<String>() {
            @Override
            public void onCompleted() {
                Log.i(TAG_RxJava, "观察者执行，onCompleted()");
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {
                Log.i(TAG_RxJava, "被观察者--》》向观察者发送数据：" + s);
            }
        };
        /*被观测者.订阅(观察者)*/
        observable.subscribe(observer);
    }

    /**
     * Observable使用from()方法创建
     * from()创建事件队列，可以一个数组或 Iterable[迭代器] 拆分成具体对象后，依次发送出来
     */
    private void from02() {
        /*快速创建 Observable 被观察者 */
        String[] array = new String[]{"Hello World", "Hello JiKeXueYuan", "Hello Key"};
        Observable<String> observable = Observable.from(array);

        /*Observer:观察者*/
        Observer<String> observer = new Observer<String>() {
            @Override
            public void onCompleted() {
                Log.i(TAG_RxJava, "观察者执行，onCompleted()");
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {
                Log.i(TAG_RxJava, "被观察者--》》向观察者发送数据：" + s);
            }
        };

        observable.subscribe(observer);
    }

    /**
     * Observable使用just()方法创建
     * just()快捷创建事件队列
     */
    private void just03() {
        /*Observable:被观察者[类似Button]*/
        Observable<String> observable = Observable.just("Hello Java", "RxJava", "jikexueyaun");
        /*Observer:观察者*/
        Observer<String> observer = new Observer<String>() {
            @Override
            public void onCompleted() {
                Log.i(TAG_RxJava, "观察者执行，onCompleted()");
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {
                Log.i(TAG_RxJava, "被观察者--》》向观察者发送数据：" + s);
            }
        };
        observable.subscribe(observer);
    }

    /**
     * subscribe()不完整的定义回调
     */
    private void subscribe04() {
        /*Observable:被观察者*/
        Observable<String> observable = Observable.just("Hello Java", "RxJava", "jikexueyaun");
        /*观察者onNext()*/
        Action1 onNextAction = new Action1() {
            @Override
            public void call(Object o) {
                String string = (String) o;
                Log.i("观察者：onNextAction:call(Object o):o:", string);
            }
        };
        /*观察者onError()*/
        Action1<Throwable> onErrorAction = new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                Log.i("观察者：onErrorAction:call(Throwable throwable):", throwable.getMessage());
            }
        };

        Action0 onCompletedAction = new Action0() {
            @Override
            public void call() {
                Log.i(TAG_RxJava, "观察者：onCompletedAction");
            }
        };
        /*可以订阅任何一个 或者 三个都可以*/
        observable.subscribe(onNextAction, onErrorAction, onCompletedAction);
    }

    /**
     * Map()操作符的使用
     * 简单使用 把int类型转换为String类型
     */
    private void map05() {
        Integer[] array = new Integer[]{1, 2, 3, 4};
        /*Observable:被观察者*/
        Observable<Integer> observable = Observable.from(array);
        /*Observer:观察者*/
        Observer<String> observer = new Observer<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {
                Log.i(TAG_RxJava, "Map-->:" + s);
            }
        };

        observable.map(new Func1<Integer, String>() {
            @Override
            public String call(Integer integer) {
                return Integer.toString(integer);
            }
        }).subscribe(observer);


    }

    /**
     * Map()操作符的使用
     * 简单使用 把int类型转换为String类型
     * 并简化 使用Action1不完整的定义回调
     * 简化  Observable<Integer> observable = Observable.from(array);
     */
    private void map06() {
        Integer[] array = new Integer[]{1, 2, 3, 4};
        /*Observable:被观察者*/
        Observable.from(array)
                .map(new Func1<Integer, String>() {
                    @Override
                    public String call(Integer integer) {
                        return Integer.toString(integer);
                    }
                })
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        Log.i(TAG_RxJava, "Map-->:" + s);
                    }
                });


    }

    /**
     * FlatMap()的使用方式
     */
    private void flatMap07() {
        Observable.from(DataUtils.getData())
                .flatMap(new Func1<Author, Observable<?>>() {
                    @Override
                    public Observable<?> call(Author author) {
                        Log.i(TAG_RxJava, author.name);
                        return Observable.from(author.Articles);

                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Object>() {

                    @Override
                    public void call(Object article) {
                        Log.i(TAG_RxJava, article.toString());
                    }
                });
    }

    /**
     * Scheduler的api
     */

    private void scheduler08() {

        Observable.from(DataUtils.getData2())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        Log.i(TAG_RxJava, String.valueOf(integer));
                    }
                });

    }
}
