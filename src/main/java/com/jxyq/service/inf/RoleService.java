package com.jxyq.service.inf;

import com.jxyq.model.Role;

public interface RoleService {
    Role selRoleByPerm(String perm);

    Role selRoleByName(String name);
}
