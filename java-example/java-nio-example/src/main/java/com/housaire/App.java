package com.housaire;

import com.housaire.reactor1.Reactor;
import com.housaire.reactor2.MainReactor;

import java.io.IOException;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws IOException, InterruptedException
    {
        Thread reactor = new Thread(new Reactor(15326));
//        Thread reactor = new Thread(new MainReactor(15326));
        reactor.start();
        reactor.join();
    }

}
