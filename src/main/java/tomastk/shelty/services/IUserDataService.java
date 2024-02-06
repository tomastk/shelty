package tomastk.shelty.services;

import tomastk.shelty.models.entities.UserData;

public interface IUserDataService {
    UserData save(UserData data);
    UserData findById(long id);
    boolean existsById(long id);
}
