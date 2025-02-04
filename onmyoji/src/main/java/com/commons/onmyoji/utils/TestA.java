package com.commons.onmyoji.utils;


import com.commons.onmyoji.components.OnmyojiKeyListenerV2;

import javax.swing.*;
import java.awt.*;

public class TestA {

    public static void main(String[] args) {
        OnmyojiKeyListenerV2 listener = new OnmyojiKeyListenerV2();
        Thread thread = new Thread(listener);
        thread.start();

    }







}
