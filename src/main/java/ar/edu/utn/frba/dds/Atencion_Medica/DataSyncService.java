package ar.edu.utn.frba.dds.Atencion_Medica;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class DataSyncService {
    @Autowired
    private DataSyncRepositorio dataSyncRepository;

    //(cron = "0 */2 * * * ?")  // CADA 2 MINUTOS
    //(cron = "0 18 12 * * ?")  // TODOS LOS DIAS A LAS 23:50
    @Scheduled(cron = "0 */2 * * * ?")
    public void synchronizeData() {
        List<Map<String, Object>> dataFromPrimary = dataSyncRepository.getDataMaster();
        dataSyncRepository.clearSlaveTable();
        dataSyncRepository.insertDataToSlave(dataFromPrimary);
    }
}