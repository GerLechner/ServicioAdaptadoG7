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

    // CRONTASK de sincronizacion
    @Scheduled(cron = "50 23 * * * ?") //TODOS LOS DIAS 2350

    public void synchronizeData() {
            List<Map<String, Object>> dataFromPrimary = dataSyncRepository.getDataMaster();

            dataSyncRepository.clearSlaveTable();

            dataSyncRepository.insertDataToSlave(dataFromPrimary);
    }
}
