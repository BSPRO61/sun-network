package org.tron.walletcli;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.tron.walletcli.checker.DepositChecker;
import org.tron.walletcli.checker.MappingChecker;
import org.tron.walletcli.checker.OracleChecker;
import org.tron.walletcli.checker.WithdrawChecker;

public class Monitor {


  public static void main(String[] s) {
    System.out.println("start...");

    DepositChecker depositChecker = new DepositChecker();
    new Thread(depositChecker::checkDeposit).start();

    MappingChecker mappingChecker = new MappingChecker();
    new Thread(mappingChecker::checkMapping).start();

    WithdrawChecker withdrawChecker = new WithdrawChecker();
    new Thread(withdrawChecker::checkWithdraw).start();

    Runnable runnable7 = () -> {
      depositChecker.println();
      mappingChecker.println();
      withdrawChecker.println();
    };

    OracleChecker oc = new OracleChecker();
    ScheduledExecutorService service = Executors
        .newScheduledThreadPool(10);
    //service.scheduleAtFixedRate(oc::checkOracleResourcesTask, 0, oc.checkTimeInterval, TimeUnit.SECONDS);
    service.scheduleWithFixedDelay(depositChecker::checkFailedDeposit, 0, oc.checkTimeInterval,
        TimeUnit.SECONDS);
    service.scheduleWithFixedDelay(mappingChecker::checkFailedMapping, 0, oc.checkTimeInterval,
        TimeUnit.SECONDS);
    service.scheduleWithFixedDelay(withdrawChecker::checkFailedWithdraw, 0, oc.checkTimeInterval,
        TimeUnit.SECONDS);
    service.scheduleWithFixedDelay(runnable7, 0, 10, TimeUnit.SECONDS);

    System.out.println("complete...");

  }

}
