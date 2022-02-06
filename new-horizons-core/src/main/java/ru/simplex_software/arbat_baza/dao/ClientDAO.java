package ru.simplex_software.arbat_baza.dao;

import net.sf.autodao.Dao;
import net.sf.autodao.Finder;
import net.sf.autodao.Limit;
import net.sf.autodao.Named;
import net.sf.autodao.Offset;
import ru.simplex_software.arbat_baza.model.clients.Client;

import java.util.List;

public interface ClientDAO extends Dao<Client, Long> {

    @Finder(query = "FROM Client WHERE UPPER(name) LIKE UPPER(CONCAT('%', :name, '%'))")
    List<Client> findByNameLike(@Named("name") String name);

    @Finder(query = "FROM Client client WHERE " +
            "client.class = NaturalClient AND " +
            "UPPER(name) LIKE UPPER(CONCAT('%', :name, '%')) " +
            "ORDER BY name ASC")
    List<Client> findNaturalByNameLike(@Named("name") String name);

    @Finder(query = "FROM Client client WHERE " +
            "(:name IS NULL OR :name = '' OR UPPER(name) LIKE UPPER(CONCAT('%', :name, '%'))) AND " +
            "(:email IS NULL OR :email = '' OR :email = email) AND " +
            "(:phone IS NULL OR :phone = '' OR :phone = phone) AND " +
            "(:mobilePhone IS NULL OR :mobilePhone = '' OR :mobilePhone = mobilePhone) AND" +
            "(:inn IS NULL OR :inn = '' OR :inn = inn) AND " +
            "(:ogrn IS NULL OR :ogrn = '' OR :ogrn = ogrn) AND " +
            "(:site IS NULL OR :site = '' OR UPPER(site) LIKE UPPER(CONCAT('%', :site, '%'))) AND " +
            "(((client.class = NaturalClient) AND :natural = TRUE) OR " +
            "((client.class = JuridicalClient) AND :juridical = TRUE)) AND " +
            "deleted = FALSE " +
            "ORDER BY name ASC")
    List<Client> findByFilter(@Named("name") String name, @Named("email") String email,
                              @Named("phone") String phone, @Named("mobilePhone") String mobilePhone,
                              @Named("inn") String inn, @Named("ogrn") String ogrn,
                              @Named("site") String site,
                              @Named("natural") boolean natural, @Named("juridical") boolean juridical);

    @Finder(query = "SELECT COUNT(*) FROM Client client WHERE " +
            "(:name IS NULL OR :name = '' OR UPPER(name) LIKE UPPER(CONCAT('%', :name, '%'))) AND " +
            "(:email IS NULL OR :email = '' OR :email = email) AND " +
            "(:phone IS NULL OR :phone = '' OR :phone = phone) AND " +
            "(:mobilePhone IS NULL OR :mobilePhone = '' OR :mobilePhone = mobilePhone) AND" +
            "(:inn IS NULL OR :inn = '' OR :inn = inn) AND " +
            "(:ogrn IS NULL OR :ogrn = '' OR :ogrn = ogrn) AND " +
            "(:site IS NULL OR :site = '' OR UPPER(site) LIKE UPPER(CONCAT('%', :site, '%'))) AND " +
            "(((client.class = NaturalClient) AND :natural = TRUE) OR " +
            "((client.class = JuridicalClient) AND :juridical = TRUE)) AND " +
            "deleted = FALSE")
    long countByFilter(@Named("name") String name, @Named("email") String email,
                       @Named("phone") String phone, @Named("mobilePhone") String mobilePhone,
                       @Named("inn") String inn, @Named("ogrn") String ogrn,
                       @Named("site") String site,
                       @Named("natural") boolean natural, @Named("juridical") boolean juridical);

    @Finder(query = "SELECT id FROM Client client WHERE " +
            "(:name IS NULL OR :name = '' OR UPPER(name) LIKE UPPER(CONCAT('%', :name, '%'))) AND " +
            "(:email IS NULL OR :email = '' OR :email = email) AND " +
            "(:phone IS NULL OR :phone = '' OR :phone = phone) AND " +
            "(:mobilePhone IS NULL OR :mobilePhone = '' OR :mobilePhone = mobilePhone) AND" +
            "(:inn IS NULL OR :inn = '' OR :inn = inn) AND " +
            "(:ogrn IS NULL OR :ogrn = '' OR :ogrn = ogrn) AND " +
            "(:site IS NULL OR :site = '' OR UPPER(site) LIKE UPPER(CONCAT('%', :site, '%'))) AND " +
            "(((client.class = NaturalClient) AND :natural = TRUE) OR " +
            "((client.class = JuridicalClient) AND :juridical = TRUE)) AND " +
            "deleted = FALSE " +
            "ORDER BY name ASC")
    List<Long> findIdsByFilter(@Named("name") String name, @Named("email") String email,
                               @Named("phone") String phone, @Named("mobilePhone") String mobilePhone,
                               @Named("inn") String inn, @Named("ogrn") String ogrn,
                               @Named("site") String site,
                               @Named("natural") boolean natural, @Named("juridical") boolean juridical,
                               @Limit int limit, @Offset int offset);

    @Finder(query = "SELECT COUNT(*) > 0 FROM Client WHERE " +
            "(:email IS NULL OR :email = '' OR :email = email) AND " +
            "id <> :id AND deleted = FALSE")
    boolean hasDuplicateByEmail(@Named("email") String email, @Named("id") long id);

    @Finder(query = "SELECT COUNT(*) > 0 FROM Client WHERE " +
            "(:phone IS NULL OR :phone = '' OR :phone = phone) AND " +
            "id <> :id AND deleted = FALSE")
    boolean hasDuplicateByPhone(@Named("phone") String phone, @Named("id") long id);

    @Finder(query = "SELECT COUNT(*) > 0 FROM Client WHERE " +
            "(:inn IS NULL OR :inn = '' OR :inn = inn) AND " +
            "id <> :id AND deleted = FALSE")
    boolean hasDuplicateByINN(@Named("inn") String inn, @Named("id") long id);

    @Finder(query = "SELECT COUNT(*) > 0 FROM Client WHERE " +
            "(:ogrn IS NULL OR :ogrn = '' OR :ogrn = ogrn) AND " +
            "id <> :id AND deleted = FALSE")
    boolean hasDuplicateByOGRN(@Named("ogrn") String ogrn, @Named("id") long id);
}
