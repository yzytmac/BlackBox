package top.niunaijun.blackbox.server;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.res.Configuration;

import java.util.HashMap;
import java.util.Map;

import top.niunaijun.blackbox.BlackBoxCore;
import top.niunaijun.blackbox.client.BClient;

/**
 * Created by Milk on 4/1/21.
 * * ∧＿∧
 * (`･ω･∥
 * 丶　つ０
 * しーＪ
 * 此处无Bug
 */
public class ClientJobServiceManager {
    private static ClientJobServiceManager sServiceManager = new ClientJobServiceManager();
    private Map<Integer, JobRecord> mJobRecords = new HashMap<>();

    public static ClientJobServiceManager get() {
        return sServiceManager;
    }

    public boolean onStartJob(JobParameters params) {
        try {
            return getJobService(params.getJobId()).onStartJob(params);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean onStopJob(JobParameters params) {
        JobService jobService = getJobService(params.getJobId());
        boolean b = jobService.onStopJob(params);
        jobService.onDestroy();
        synchronized (mJobRecords) {
            mJobRecords.remove(params.getJobId());
        }
        return b;
    }

    public void onConfigurationChanged(Configuration newConfig) {
        for (JobRecord jobRecord : mJobRecords.values()) {
            if (jobRecord.mJobService != null) {
                jobRecord.mJobService.onConfigurationChanged(newConfig);
            }
        }
    }

    public void onDestroy() {
//        for (JobRecord jobRecord : mJobRecords.values()) {
//            if (jobRecord.mJobService != null) {
//                jobRecord.mJobService.onDestroy();
//            }
//        }
    }

    public void onLowMemory() {
        for (JobRecord jobRecord : mJobRecords.values()) {
            if (jobRecord.mJobService != null) {
                jobRecord.mJobService.onLowMemory();
            }
        }
    }

    public void onTrimMemory(int level) {
        for (JobRecord jobRecord : mJobRecords.values()) {
            if (jobRecord.mJobService != null) {
                jobRecord.mJobService.onTrimMemory(level);
            }
        }
    }

    JobService getJobService(int jobId) {
        synchronized (mJobRecords) {
            JobRecord jobRecord = mJobRecords.get(jobId);
            if (jobRecord != null && jobRecord.mJobService != null) {
                return jobRecord.mJobService;
            }
            try {
                JobRecord record = BlackBoxCore.getVJobManager().queryJobRecord(BClient.getClientConfig().processName, jobId);
                record.mJobService = BClient.getClient().createJobService(record.mServiceInfo);
                mJobRecords.put(jobId, record);
                return record.mJobService;
            } catch (Throwable t) {
                t.printStackTrace();
            }
            return null;
        }
    }
}
