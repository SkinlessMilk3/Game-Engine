package Components.TestGame;

import Components.Component;

public class EnemyLoop extends Component {
    @Override
    public void start() {

    }

    @Override
    public void update(float dt) {
        gameObject.transform.position.x -= 0.025;
    }
}
