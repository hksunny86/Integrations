package com.inov8.integration.boss.controller;

import java.util.Map;

public interface IBOSSSwitchController {
    public BOSSVO topup(BOSSVO bossvo);

    public BOSSVO topupRollback(BOSSVO bossvo);

    public BOSSVO dealerTopup(BOSSVO bossvo);

    public BOSSVO dealerTopupRollback(BOSSVO bossvo);

    public BOSSVO checkSubscriber(BOSSVO bossvo);

    public BOSSVO rechargeBalance(BOSSVO bossvo);

    public BOSSVO balanceQuery(BOSSVO bossvo);

    public Map<Object, Object> doAnything(Map<Object, Object> params);

    public LimitEnhancementVO limitEnhancement(LimitEnhancementVO limitEnhancementVO);


}
