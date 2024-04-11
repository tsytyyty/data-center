package com.data.center.service;

import java.util.Map;
import java.util.concurrent.ExecutionException;

public interface DataAcquisitionService {

    Map<String, Integer> dataAcquisition() throws InterruptedException, ExecutionException;

}
