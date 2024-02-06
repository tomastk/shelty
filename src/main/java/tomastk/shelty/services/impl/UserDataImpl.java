package tomastk.shelty.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tomastk.shelty.models.daos.UserDataDAO;
import tomastk.shelty.models.entities.UserData;
import tomastk.shelty.services.IUserDataService;

@Service
public class UserDataImpl implements IUserDataService {

    @Autowired
    private UserDataDAO userDataDAO;

    @Override
    public UserData save(UserData data) {
        return userDataDAO.save(data);
    }

    @Override
    public UserData findById(long ownerId) {
            return userDataDAO.findById(ownerId).orElse(null);
    }

    @Override
    public boolean existsById(long id) {
        return userDataDAO.existsById(id);
    }
}
