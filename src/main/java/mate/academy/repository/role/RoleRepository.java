package mate.academy.repository.role;

import mate.academy.model.Role;
import mate.academy.model.Role.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByRole(RoleName roleName);
}
