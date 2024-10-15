package cat.itacademy.s05.t01.n01.repositories;

import org.springframework.stereotype.Repository;

@Repository
public class PlayerRepository extends JpaRepository<Player, Integer> {

}
