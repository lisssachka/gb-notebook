package notebook.repository.impl;


import notebook.model.User;
import notebook.mapper.impl.UserMapper;
import notebook.dao.impl.FileOperation;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import notebook.model.repository.GBRepository;

public class UserRepository implements GBRepository<User, Long> {
    private final UserMapper mapper;
    private final FileOperation operation;

    public UserRepository(FileOperation operation) {
        this.mapper = new UserMapper();
        this.operation = operation;
    }

    @Override
    public List<User> findAll() {
        List<String> lines = operation.readAll();
        List<User> users = new ArrayList<>();
        for (String line : lines) {
            users.add(mapper.toOutput(line));
        }
        return users;
    }

    @Override
    public User create(User user) {
        List<User> users = findAll();
        long max = 0L;
        for (User u : users) {
            long id = u.getId();
            if (max < id){
                max = id;
            }
        }
        long next = max + 1;
        user.setId(next);
        users.add(user);
        List<String> lines = new ArrayList<>();
        for (User u: users) {
            lines.add(mapper.toInput(u));
        }
        operation.saveAll(lines);
        return user;
    }

    @Override
    public Optional<User> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public Optional<User> update(Long userId, User update) {
        List<User> users = findAll();
        User editUser = users.stream()
                .filter(u -> u.getId()
                        .equals(userId))
                .findFirst().orElseThrow(() -> new RuntimeException("User not found"));
        editUser.setFirstName(update.getFirstName());
        editUser.setLastName(update.getLastName());
        editUser.setPhone(update.getPhone());
        write(users);
        return Optional.of(update);
    }

    @Override
    public boolean delete(Long id) {
        try{
            List<User> users = findAll();
            User delUser = users.stream().filter(u -> u.getId().equals(id)).findFirst().get();
            for (User user: users) {
                if (user.getId() > delUser.getId())
                    user.setId(user.getId()-1);
            }
            users.remove(delUser);
            List<String> list = new ArrayList<>();
            for (User user: users) {
                list.add(mapper.toInput(user));
            }
            operation.saveAll(list);
            return  true;
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public UserMapper getMapper() {

        return mapper;
    }
    private void write(List<User> users) {
        List<String> lines = new ArrayList<>();
        for (User u: users) {
            lines.add(mapper.toInput(u));
        }
        operation.saveAll(lines);
    }
}

