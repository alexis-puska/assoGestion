package fr.iocean.asso.service;

import fr.iocean.asso.domain.Absence;
import fr.iocean.asso.domain.User;
import fr.iocean.asso.repository.AbsenceRepository;
import fr.iocean.asso.service.dto.AbsenceDTO;
import fr.iocean.asso.service.mapper.AbsenceMapper;
import fr.iocean.asso.web.rest.errors.AbsenceDateException;
import fr.iocean.asso.web.rest.errors.AbsenceMustHaveUserException;
import fr.iocean.asso.web.rest.errors.AbsenceUserNotMatchException;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Absence}.
 */
@Service
@Transactional
public class AbsenceService {

    private final Logger log = LoggerFactory.getLogger(AbsenceService.class);

    private final AbsenceRepository absenceRepository;

    private final UserService userService;

    private final AbsenceMapper absenceMapper;

    public AbsenceService(AbsenceRepository absenceRepository, UserService userService, AbsenceMapper absenceMapper) {
        this.absenceRepository = absenceRepository;
        this.userService = userService;
        this.absenceMapper = absenceMapper;
    }

    /**
     * Save a absence.
     *
     * @param absenceDTO the entity to save.
     * @return the persisted entity.
     */
    public AbsenceDTO save(AbsenceDTO absenceDTO) {
        log.debug("Request to save Absence : {}", absenceDTO);
        this.checkDate(absenceDTO);
        Absence absence = absenceMapper.toEntity(absenceDTO);
        Optional<User> currentUser = this.userService.getUserWithAuthorities();
        absence.setUser(currentUser.get());
        absence = absenceRepository.save(absence);
        return absenceMapper.toDto(absence);
    }

    /**
     * Get all the absences.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<AbsenceDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Absences");
        Optional<User> currentUser = this.userService.getUserWithAuthorities();
        return absenceRepository.findAllByUser(pageable, currentUser.get()).map(absenceMapper::toDto);
    }

    /**
     * Get one absence by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AbsenceDTO> findOne(Long id) {
        log.debug("Request to get Absence : {}", id);
        Optional<User> currentUser = this.userService.getUserWithAuthorities();
        Optional<Absence> absenceOptional = absenceRepository.findById(id);
        if (absenceOptional.isEmpty()) {
            throw new AbsenceMustHaveUserException();
        }
        Absence absence = absenceOptional.get();
        if (absence.getUser().getId() != currentUser.get().getId()) {
            throw new AbsenceUserNotMatchException();
        }
        return Optional.of(absenceMapper.toDto(absence));
    }

    /**
     * Delete the absence by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Absence : {}", id);
        Optional<User> currentUser = this.userService.getUserWithAuthorities();
        Optional<Absence> absenceOptional = absenceRepository.findById(id);
        if (absenceOptional.isEmpty()) {
            throw new AbsenceMustHaveUserException();
        }
        Absence absence = absenceOptional.get();
        if (absence.getUser().getId() != currentUser.get().getId()) {
            throw new AbsenceUserNotMatchException();
        }
        absenceRepository.deleteById(id);
    }

    /**
     * Save a absence.
     *
     * @param absenceDTO the entity to save.
     * @return the persisted entity.
     */
    public AbsenceDTO saveAdmin(AbsenceDTO absenceDTO) {
        log.debug("Request to save Absence : {}", absenceDTO);
        this.checkDate(absenceDTO);
        Absence absence = absenceMapper.toEntity(absenceDTO);
        absence = absenceRepository.save(absence);
        return absenceMapper.toDto(absence);
    }

    /**
     * Get all the absences.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<AbsenceDTO> findAllAdmin(Pageable pageable) {
        log.debug("Request to get all Absences");
        return absenceRepository.findAll(pageable).map(absenceMapper::toDto);
    }

    /**
     * Get one absence by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AbsenceDTO> findOneAdmin(Long id) {
        log.debug("Request to get Absence : {}", id);
        return absenceRepository.findById(id).map(absenceMapper::toDto);
    }

    /**
     * Delete the absence by id.
     *
     * @param id the id of the entity.
     */
    public void deleteAdmin(Long id) {
        log.debug("Request to delete Absence : {}", id);
        absenceRepository.deleteById(id);
    }

    private void checkDate(AbsenceDTO absence) {
        if (absence.getStart().isAfter(absence.getEnd())) {
            throw new AbsenceDateException();
        }
    }
}
