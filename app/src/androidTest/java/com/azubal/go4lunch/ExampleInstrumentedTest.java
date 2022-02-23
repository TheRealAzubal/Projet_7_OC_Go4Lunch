package com.azubal.go4lunch;

import static com.azubal.go4lunch.LiveDataTestUtil.getValue;

import android.app.Application;
import android.content.Context;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import com.azubal.go4lunch.viewmodels.UserViewModel;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();
    UserViewModel userViewModel;
    FirebaseAuth firebaseAuth;
    Application context;


    @Before
    public void setUp(){
        context = ApplicationProvider.getApplicationContext();
        userViewModel = new UserViewModel(context);
        firebaseAuth =FirebaseAuth.getInstance();
    }


    @Test
    public void isCurrentUserLoggedIn() throws InterruptedException{
        assertEquals(false,LiveDataTestUtil.getValue(userViewModel.isCurrentUserLoggedIn()));
    }


    @Test
    public void signOut() throws InterruptedException{
        firebaseAuth.signInWithEmailAndPassword("test@gmail.com","test123");
        userViewModel.signOut();
        assertEquals(false,LiveDataTestUtil.getValue(userViewModel.isCurrentUserLoggedIn()));
    }







}