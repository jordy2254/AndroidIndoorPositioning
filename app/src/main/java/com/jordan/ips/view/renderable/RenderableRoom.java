package com.jordan.ips.view.renderable;


import com.jordan.ips.model.data.map.persisted.Room;
import com.jordan.ips.model.data.map.persisted.RoomIndent;
import com.jordan.ips.model.exceptions.InvalidLocationException;
import com.jordan.ips.model.utils.IndentLocationFinder;
import com.jordan.renderengine.Screen;
import com.jordan.renderengine.data.Pair;
import com.jordan.renderengine.data.Point2d;
import com.jordan.renderengine.graphics.Renderable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RenderableRoom implements Renderable {

    Room room;
    private boolean wallsCalculated = false;
    public List<Pair<Point2d,Point2d>> walls;

    public RenderableRoom(Room room) {
        this.room = room;
    }

    /**
     * Rendering code. Just renders the wall in the correct place. and calls calculate walls.
     */
    @Override
    public void render(Screen screen, Point2d offset, double scale) {
//        if(!room.getName().equals("Bathroom")){
//            return;
////        }
//        if(room.getId() != 1) return;

        calculateWalls();
        List<Point2d> polygon = createPolygon((int)offset.x, (int)offset.y, scale);
//        screen.fillNonRecursive(0,0, 0xff00ff);

        screen.drawPolygonUpdated(polygon, 2, 0x0, 0xf0ae5d, true);
        screen.drawPolygon(polygon, 2, 0x0, true);
    }
//    @Override
//    public void draw(Canvas canvas, Graphics g, int offX, int offY, float scale) {
//        calculateWalls();
//        Graphics2D g2 = (Graphics2D)g;
//        float thickness = 4;
//        g2.setStroke(new BasicStroke(thickness));
//        g2.setColor(Color.BLACK);
//
//        Polygon p = createRenderablePolygon(offX, offY, scale);
//        g2.setColor(new Color(0xFED77E));
//        g2.fillPolygon(p);
//        g2.setColor(new Color(0xA97600));
//        g2.drawPolygon(p);
//        g2.setColor(Color.BLACK);
//        double averagex = Arrays.stream(p.xpoints).sum() / p.npoints;
//        double averagey = Arrays.stream(p.ypoints).sum()/ p.npoints;
//        g2.drawString(room.getName(), (int)averagex, (int)averagey);
//    }

    private List<Point2d> createPolygon(int offX, int offY, double scale){
        List<Point2d> p = new ArrayList<>();
        Pair<Point2d, Point2d> firstPair = null;
        Pair<Point2d, Point2d> lastPair = null;

        int panic = 0;
        while((panic ++) != walls.size() * 2 + 1){
            if(firstPair == null){
                firstPair = walls.get(0);
                lastPair = firstPair;
                p.add(new Point2d(((firstPair.fst.x * scale)) + offX, ((firstPair.fst.y * scale) ) + offY));
                continue;
            }
            if(lastPair.snd.equals(firstPair.fst)){
                break;
            }
            Pair<Point2d, Point2d> current = null;
            for(Pair<Point2d, Point2d> tmp : walls){
                if(tmp.fst.equals(lastPair.snd)){
                    current = tmp;
                    break;
                }
            }

            if(current == null){
                break;
            }
            p.add(new Point2d((current.fst.x * scale) + offX,
                    ((current.fst.y * scale)) + offY));
            lastPair = current;
        }
        if(panic == walls.size() *2 +1){
            System.err.println("Room: " + room.getName() + "is invalid");
            return null;
        }

//        if(room.getId() == 1)
//            room.setRotation(Math.toRadians(5));

//        for (Point2d point : p) {
//            Point2d origin = new Point2d(room.getDimensions().x / 2, room.getDimensions().y / 2);
//            double cos = Math.cos(room.getRotation());
//            double sin = Math.sin(room.getRotation());
//
//            // translate point back to origin:
//            point.x -= origin.x;
//            point.y -= origin.y;
//
//            // rotate point
//            double xnew = point.x * cos - point.y * sin;
//            double ynew = point.x * sin + point.y * cos;
//
//            // translate point back:
//            point.x = xnew + origin.x;
//            point.y = ynew + origin.y;
//        }
        for(Point2d point : p){
            point.x += room.getLocation().x * scale;
            point.y += room.getLocation().y * scale;
        }
//        p.translate((int)(room.getxLocation() * scale), (int)(room.getyLocation() * scale));
        return p;
    }

    public boolean linesIntersect(Point2d p1, Point2d p2, Point2d p3, Point2d p4){

        //vertical line
        if(p1.x == p3.x && p2.x == p3.x && p4.x == p1.x){
            //posative increase
            if(p1.y - p2.y < 0){
                return p3.y >= p1.y && p3.y <= p2.y;
            }

            //negative increase
            if(p1.y - p2.y > 0){
                return p3.y <= p1.y && p3.y >= p2.y;
            }
        }

        //Horazontal line
        if(p1.y == p3.y && p2.y == p3.y && p4.y == p1.y){
            //posative increase
            if(p1.x - p2.x < 0){
                return p3.x >= p1.x && p3.x <= p2.x;
            }

            //negative increase
            if(p1.x - p2.x > 0){
                return p3.x <= p1.x && p3.x >= p2.x;
            }
        }

        return false;
    }

    private List<Pair<Point2d,Point2d>> calculateRectangleEdgePairs(double x, double y, double width, double height){
        List<Pair<Point2d,Point2d>> points = new ArrayList<>();
        Point2d tl = new Point2d(x, y);
        Point2d tr = new Point2d(x + width, y);
        Point2d br = new Point2d(x + width, y + height);
        Point2d bl = new Point2d(x, y + height);

        points.add(new Pair<>(tl, tr));
        points.add(new Pair<>(tr, br));
        points.add(new Pair<>(br, bl));
        points.add(new Pair<>(bl, tl));

        return points;
    }



    public void calculateWalls() {
        if(wallsCalculated){
            return;
        }

        //calculate all indent edges
        List<Pair<Point2d,Point2d>> roomEdges = new ArrayList<>();
        roomEdges.addAll(calculateRectangleEdgePairs(0, 0, room.getDimensions().x, room.getDimensions().y));
        List<Pair<Point2d,Point2d>> indentEdges = new ArrayList<>();


        for(RoomIndent indent : room.getIndents()){
            try {
                double[] startLocations = IndentLocationFinder.findStartPointsOfIndent(room, indent, true);
                indentEdges.addAll(calculateRectangleEdgePairs(startLocations[0], startLocations[1], indent.getDimensions().x, indent.getDimensions().y));
            } catch (InvalidLocationException e) {
                e.printStackTrace();
            }
        }

        //Calculate overlapping edges to remove.
        List<Pair<Point2d, Point2d>> toRemove = new ArrayList<>();

        for(Pair<Point2d, Point2d> indentWall : indentEdges){
            Pair<Point2d, Point2d> found  = null;

            //if we ever overlap on more than one edge someone has an invalid setup
            for(Pair<Point2d, Point2d> roomWall : roomEdges){
                if(linesIntersect(roomWall.fst, roomWall.snd, indentWall.fst, indentWall.snd)){
                    found = roomWall;
                    break;
                }
            }

            if(found != null){
                Point2d tmp = new Point2d(found.snd);
                found.snd = indentWall.fst;
                if(!indentWall.snd.equals(tmp)){
                    roomEdges.add(new Pair<>(indentWall.snd, tmp));
                }
                if(found.fst.equals(found.snd)){
                    roomEdges.remove(found);
                }
                toRemove.add(indentWall);
            }
        }

        //remove the overlapping edges.
        indentEdges.removeAll(toRemove);
        roomEdges.removeAll(toRemove);


        //validate indent edge points are the correct way around for a valid polygon.
        for(Pair<Point2d, Point2d> tmpWall : indentEdges){
            List<Pair<Point2d, Point2d>> allWalls = new ArrayList<>();
            allWalls.addAll(roomEdges);
            allWalls.addAll(indentEdges);
            List<Point2d> sndEdges = allWalls.stream().map(p -> p.snd).collect(Collectors.toList());
            List<Point2d> fstEdges = allWalls.stream().map(p -> p.fst).collect(Collectors.toList());

            long fstCount = fstEdges.stream().filter(point -> point.equals(tmpWall.fst)).count();
            long sndCount = sndEdges.stream().filter(point -> point.equals(tmpWall.fst)).count();

            long fstCount2 = fstEdges.stream().filter(point -> point.equals(tmpWall.snd)).count();
            long sndCount2 = sndEdges.stream().filter(point -> point.equals(tmpWall.snd)).count();

            //if our points are the wrong way round
            if(fstCount != sndCount || fstCount2 != sndCount2){
                Point2d tmp = tmpWall.fst;
                tmpWall.fst = tmpWall.snd;
                tmpWall.snd = tmp;
            }
        }
        walls = new ArrayList<>();
        walls.addAll(roomEdges);
        walls.addAll(indentEdges);

        wallsCalculated = true;
    }

}
