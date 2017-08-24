package life.rnl.transformer;

public interface Transformer<View, Entity> {
	Entity compose(View item);
	
	View decompose(Entity item);
}
