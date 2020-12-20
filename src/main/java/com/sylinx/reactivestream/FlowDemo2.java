package com.sylinx.reactivestream;

import java.util.concurrent.Flow;
import java.util.concurrent.SubmissionPublisher;
import java.util.concurrent.TimeUnit;

public class FlowDemo2 {

    public static void main(String []args) throws Exception{
        SubmissionPublisher<Integer> publiser = new SubmissionPublisher<>();
        MyProcessor myProcessor = new MyProcessor();
        publiser.subscribe(myProcessor);

        Flow.Subscriber<String> subscriber = new Flow.Subscriber<String>() {
            private Flow.Subscription subscription;
            @Override
            public void onSubscribe(Flow.Subscription subscription) {
                this.subscription = subscription;
            }

            @Override
            public void onNext(String item) {
                System.out.println("finish reciver : " + item);
                this.subscription.request(1);
            }

            @Override
            public void onError(Throwable throwable) {
                throwable.printStackTrace();
                this.subscription.cancel();
            }

            @Override
            public void onComplete() {
                System.out.println("string finish");
            }
        };

        myProcessor.subscribe(subscriber);
        // submit是个block方式
//        for (int i=0;i<1000;i++) {
//            System.out.println("generate : " + i);
//            publiser.submit(i);
//        }
            publiser.submit(111);
            publiser.submit(-1);
//            publiser.submit(333);
//            publiser.submit(-4);
//            publiser.submit(555);
        publiser.close();
        Thread.currentThread().join(1000);

    }
}

class MyProcessor extends SubmissionPublisher<String> implements Flow.Processor<Integer, String> {

    private Flow.Subscription subscription;

    @Override
    public void onSubscribe(Flow.Subscription subscription) {
        this.subscription = subscription;
        this.subscription.request(1);
    }

    @Override
    public void onNext(Integer item) {
        System.out.println("process receive : " + item );
        if(item > 0) {
            this.submit("transfer : " + item);
        }
        this.subscription.request(1);

    }

    @Override
    public void onError(Throwable throwable) {
        throwable.printStackTrace();
        this.subscription.cancel();
    }

    @Override
    public void onComplete() {
        System.out.println("integer finish");
        this.close();

    }
}