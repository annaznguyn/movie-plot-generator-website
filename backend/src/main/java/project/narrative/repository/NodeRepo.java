package project.narrative.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import project.narrative.model.entities.Node;

import java.util.List;
import java.util.Optional;

@Repository
public interface NodeRepo extends JpaRepository<Node, Long>{

    Optional<List<Node>> findByStoryStoryidAndParentIdIsNull(Long storyId);

    // Get all child nodes for a specific parent node
    Optional<List<Node>> findByParentId(Long parentId);

    // Get all nodes for a specific story
    Optional<List<Node>> findByStoryStoryid(Long storyId);
}
