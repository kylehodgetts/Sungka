package com.kylehodgetts.sunka.event;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ViewFlipper;

import com.kylehodgetts.sunka.BoardActivity;
import com.kylehodgetts.sunka.R;
import com.kylehodgetts.sunka.controller.bus.Event;

/**
 * @author Adam Chlupacek
 * @version 1.0
 * Event that notifies that the current game has ended
 */
public class EndGame implements Event {}
