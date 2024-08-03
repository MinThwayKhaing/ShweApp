package com.app.shwe.service;

import com.app.shwe.model.Process;
import com.app.shwe.repository.ProcessRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProcessService {

    @Autowired
    private ProcessRepository processRepository;

    public List<Process> getAllProcesses() {
        return processRepository.findAllByOrderByImageorderAsc();
    }

    public Optional<Process> getProcessById(Integer id) {
    	  if (id == null) {
              throw new IllegalArgumentException("Id cannot be null");
          } 
        return processRepository.findById(id);
    }

    public Process addProcess(Process process) {
     	  if (process == null) {
              throw new IllegalArgumentException("Process cannot be null");
          } 
        return processRepository.save(process);
    }

    public Process updateProcess(Integer id, Process processDetails) {
    	  if (processDetails == null) {
               throw new IllegalArgumentException("Process details cannot be null");
           }  
          
    Process process = processRepository.findById(id).orElseThrow(() -> new RuntimeException("Process not found"));
    process.setName(processDetails.getName());
    process.setDescription(processDetails.getDescription());
    process.setPrice(processDetails.getPrice());
    process.setImageorder(processDetails.getImageorder());
    process.setImglink(processDetails.getImglink());
    process.setCategory(processDetails.getCategory());
        return processRepository.save(process);
    }

    
    public void deleteProcess(Integer id) {
    	
   	  if (id == null) {
          throw new IllegalArgumentException("Delete Id cannot be null");
      } 
   	  
        if (processRepository.existsById(id)) {
            processRepository.deleteById(id);
        } else {
            throw new RuntimeException("Process not found");
        }
    }
}
