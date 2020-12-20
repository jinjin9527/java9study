package com.sylinx.reactivestream;

import java.util.concurrent.Flow;
import java.util.concurrent.SubmissionPublisher;

public class FlowDemo {

    public static void main(String []args) throws Exception{
        SubmissionPublisher<Integer> publiser = new SubmissionPublisher<>();
        Flow.Subscriber<Integer> subscriber = new Flow.Subscriber<>(){

            private Flow.Subscription subscription;
            @Override
            public void onSubscribe(Flow.Subscription subscription) {
                this.subscription = subscription;
                this.subscription.request(1);
            }

            @Override
            public void onNext(Integer item) {
                System.out.println(item);
                this.subscription.request(1);
                // this.subscription.cancel();

            }

            @Override
            public void onError(Throwable throwable) {
                throwable.printStackTrace();
                this.subscription.cancel();
            }

            @Override
            public void onComplete() {
                System.out.println("over");
            }
        };
        publiser.subscribe(subscriber);
        int data = 111;
        publiser.submit(111);
        publiser.submit(222);
        publiser.submit(333);
        publiser.submit(444);
        publiser.submit(555);
        publiser.close();
        Thread.currentThread().join(1000);

    }
}
