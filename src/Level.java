import java.awt.Point;


public class Level {

    private TileType[] level;

    private final int ROW_COUNT;

    private final int COL_COUNT;

    private final int numberOfSnakes;

    private final int time;

    Level(int r, int c, int numberOfSnakes){
        ROW_COUNT = r;
        COL_COUNT = c;
        level = new TileType[ROW_COUNT * COL_COUNT];
        this.setBorder();
        this.numberOfSnakes = numberOfSnakes;
        this.time = 0;
    }

    public int getTime(){
        return this.time;
    }

    public int getNumberOfSnakes(){
        return this.numberOfSnakes;
    }

    /**
     * Sets the tile at the desired coordinate.
     * @param point The coordinate of the tile.
     * @param type The type to set the tile to.
     */
    public void setTile(Point point, TileType type) {
        setTile(point.x, point.y, type);
    }

    /**
     * Sets the tile at the desired coordinate.
     * @param x The x coordinate of the tile.
     * @param y The y coordinate of the tile.
     * @param type The type to set the tile to.
     */
    public void setTile(int x, int y, TileType type) {
        level[y * ROW_COUNT + x] = type;
    }

    /**
     * Gets the tile at the desired coordinate.
     * @param x The x coordinate of the tile.
     * @param y The y coordinate of the tile.
     * @return
     */
    public TileType getTile(int x, int y) {
        return level[y * ROW_COUNT + x];
    }

    public int getRowCount(){
        return this.ROW_COUNT;
    }

    public int getColCount(){
        return this.COL_COUNT;
    }

    public void setBorder(){
        for(int i = 0; i < this.COL_COUNT; i++){
            this.setTile(new Point(i, 0), TileType.Wall);
            this.setTile(new Point(i, this.ROW_COUNT-1), TileType.Wall);
        }

        for(int i = 0; i < this.ROW_COUNT; i++){
            this.setTile(new Point(0, i), TileType.Wall);
            this.setTile(new Point(this.COL_COUNT-1, i), TileType.Wall);
        }
    }

}
