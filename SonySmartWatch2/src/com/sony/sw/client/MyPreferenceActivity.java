/*
Copyright (c) 2011, Sony Ericsson Mobile Communications AB
Copyright (c) 2011-2013, Sony Mobile Communications AB

 All rights reserved.

 Redistribution and use in source and binary forms, with or without
 modification, are permitted provided that the following conditions are met:

 * Redistributions of source code must retain the above copyright notice, this
 list of conditions and the following disclaimer.

 * Redistributions in binary form must reproduce the above copyright notice,
 this list of conditions and the following disclaimer in the documentation
 and/or other materials provided with the distribution.

 * Neither the name of the Sony Ericsson Mobile Communications AB / Sony Mobile
 Communications AB nor the names of its contributors may be used to endorse or promote
 products derived from this software without specific prior written permission.

 THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.sony.sw.client;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.util.Log;
import android.widget.Toast;

import com.sonyericsson.extras.liveware.extension.util.ExtensionUtils;
import com.sonyericsson.extras.liveware.extension.util.notification.NotificationUtil;

/**
 * The sample preference activity lets the user toggle start/stop of periodic
 * data insertion. It also allows the user to clear all events associated with
 * this extension.
 */
public class MyPreferenceActivity extends PreferenceActivity {

    private static final int DIALOG_READ_ME = 1;

    private static final int DIALOG_CLEAR = 2;

    @SuppressWarnings("deprecation")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);

        // Handle read me
        Preference preference = findPreference(getText(R.string.preference_key_read_me));
        preference.setOnPreferenceClickListener(new OnPreferenceClickListener() {

            @Override
            public boolean onPreferenceClick(Preference preference) {
                showDialog(DIALOG_READ_ME);
                return true;
            }
        });

        // Handle active
        preference = (CheckBoxPreference)findPreference(getText(R.string.preference_key_is_active));
        preference.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {

            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if ((Boolean)newValue) {
                	new AlertDialog.Builder(MyPreferenceActivity.this).setTitle("读取信息")
            		.setMessage("即将读取信息").setPositiveButton("确定",null).show();
                	FileUtils fu = new FileUtils(PPT.pptList);
                	fu.readFile();
//    				PPT ppt;
//    				for(int i = 0;i < 5;i++){
//    					ppt = new PPT(i,"2:00","test-" + i);
//    					PPT.pptList.add(ppt);
//    				}
    				
                	Toast.makeText(MyPreferenceActivity.this,"读取成功，共 " + PPT.pptList.size() + " 张PPT,祝您好运！",Toast.LENGTH_SHORT);
                    startSampleExtensionService();
                } else {
                    stopSampleExtensionService();
                }
                return true;
            }
        });

        // Handle clear all events
        preference = findPreference(getString(R.string.preference_key_clear));
        preference.setOnPreferenceClickListener(new OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                showDialog(DIALOG_CLEAR);
                return true;
            }
        });

        // Remove preferences that are not supported by the accessory
        if (!ExtensionUtils.supportsHistory(getIntent())) {
            preference = findPreference(getString(R.string.preference_key_clear));
            getPreferenceScreen().removePreference(preference);
        }
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        Dialog dialog = null;

        switch (id) {
            case DIALOG_READ_ME:
                dialog = createReadMeDialog();
                break;
            case DIALOG_CLEAR:
                dialog = createClearDialog();
                break;
            default:
                Log.w(MyService.LOG_TAG, "Not a valid dialog id: " + id);
                break;
        }

        return dialog;
    }

    /**
     * Create the Read me dialog
     *
     * @return the Dialog
     */
    private Dialog createReadMeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.preference_option_read_me_txt)
                .setTitle(R.string.preference_option_read_me)
                .setIcon(android.R.drawable.ic_dialog_info)
                .setPositiveButton(android.R.string.ok, new OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        return builder.create();
    }

    /**
     * Create the Clear events dialog
     *
     * @return the Dialog
     */
    private Dialog createClearDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.preference_option_clear_txt)
                .setTitle(R.string.preference_option_clear)
                .setIcon(android.R.drawable.ic_input_delete)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        new ClearEventsTask().execute();
                    }
                }).setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        return builder.create();
    }

    /**
     * Activate event generation
     */
    private void startSampleExtensionService() {
        Intent serviceIntent = new Intent(this, MyService.class);
        serviceIntent.setAction(MyService.INTENT_ACTION_START);
        startService(serviceIntent);
    }

    /**
     * Cancel event generation
     */
    private void stopSampleExtensionService() {
        Intent serviceIntent = new Intent(this, MyService.class);
        serviceIntent.setAction(MyService.INTENT_ACTION_STOP);
        startService(serviceIntent);
    }

    /**
     * Clear all messaging events
     */
    private class ClearEventsTask extends AsyncTask<Void, Void, Integer> {

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Integer doInBackground(Void... params) {
            int nbrDeleted = 0;
            nbrDeleted = NotificationUtil.deleteAllEvents(MyPreferenceActivity.this);
            return nbrDeleted;
        }

        @Override
        protected void onPostExecute(Integer id) {
            if (id != NotificationUtil.INVALID_ID) {
                Toast.makeText(MyPreferenceActivity.this, R.string.clear_success,
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MyPreferenceActivity.this, R.string.clear_failure,
                        Toast.LENGTH_SHORT).show();
            }
        }

    }
}
