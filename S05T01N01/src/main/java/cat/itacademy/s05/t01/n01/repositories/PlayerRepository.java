package cat.itacademy.s05.t01.n01.repositories;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import cat.itacademy.s05.t01.n01.model.Player;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Integer> {
	

}
