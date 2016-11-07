package com.alz.dailyvideonews;

/**
 * Created by Al on 2016-11-06.
 */

import ckm.simple.sql_provider.UpgradeScript;
import ckm.simple.sql_provider.annotation.ProviderConfig;
import ckm.simple.sql_provider.annotation.SimpleSQLConfig;

@SimpleSQLConfig(
        name = "VideoProvider",
        authority = "just.some.test_provider.authority",
        database = "Videos.db",
        version = 1)

public class VideoProviderConfig implements ProviderConfig {
    @Override
    public UpgradeScript[] getUpdateScripts() {
        return new UpgradeScript[0];
    }
}
