package com.jam.projects.appmedica.services;

import com.jam.projects.appmedica.dtos.PatientDto;
import com.jam.projects.appmedica.dtos.VitalSignDto;
import com.jam.projects.appmedica.entities.Patient;
import com.jam.projects.appmedica.entities.VitalSign;
import com.jam.projects.appmedica.repositories.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PatientService {

    @Autowired
    PatientRepository patientRepository;

    public Patient createPatient(PatientDto patientDto) {

        if (patientDto.getVitalSignDtoList() == null)
            throw new IllegalArgumentException("Falta la lista de signos vitales");

        return patientRepository.save(new Patient(patientDto));
    }

    public List<Patient> findAllPatientWithPagination(Integer offset, Integer pageSize) {

        return patientRepository.findAll(PageRequest.of(offset, pageSize)).getContent();
    }

    public List<Patient> findPatientsByName(String name) {

        return patientRepository.findPatientsByName(name);
    }

    public Patient findPatientById(Integer id) {

        Optional<Patient> patient = patientRepository.findById(id);

        if (patient.isEmpty())
            throw new EntityNotFoundException("Patient not found");

        return patient.get();
    }

    public List<VitalSign> findPatientVitalSignById(Integer id) {

        return findPatientById(id).getVitalSignList();
    }

    public List<VitalSign> addVitalSignByPatientId(Integer id, VitalSignDto vitalSignDto) {

        Patient patient = findPatientById(id);

        patient.getVitalSignList().add(new VitalSign(vitalSignDto));

        return patientRepository.save(patient).getVitalSignList();
    }

    public Patient updatePatientNameAndDateOfBirth(Integer id, PatientDto patientDto) {

        Patient patient = findPatientById(id);

        patient.setName(patientDto.getName());
        patient.setDateOfBirth(patientDto.getDateOfBirth());

        return patientRepository.save(patient);
    }

    public void deletePatient(Integer id) {

        Patient patient = findPatientById(id);

        patientRepository.delete(patient);
    }

    public Patient updatePatientVitalSign(Integer id, List<VitalSignDto> vitalSignDtoList) {

        Patient patient = findPatientById(id);

        List<VitalSign> vitalSignList = vitalSignDtoList.stream().map(VitalSign::new).collect(Collectors.toList());

        patient.setVitalSignList(vitalSignList);

        return patientRepository.save(patient);
    }
}
