package com.jordan.ips.model.utils.rendering;

import com.jordan.renderengine.data.Pair;
import com.jordan.renderengine.data.Point2d;
import com.jordan.ips.model.data.map.persisted.Room;
import com.jordan.ips.model.data.map.persisted.RoomIndent;
import com.jordan.ips.model.exceptions.InvalidLocationException;
import com.jordan.ips.model.utils.IndentLocationFinder;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PolygonRoomGenerator {

    public static List<Point2d> getPointListForRoom(Room room){
        List<Pair<Point2d,Point2d>> walls = calculateWalls(room);
        List<Point2d> orderedWalls = createRenderablePolygon(walls, room);
        return  orderedWalls;
    }

    public static List<Pair<Point2d,Point2d>> calculateWalls(Room room) {
        List<Pair<Point2d,Point2d>> walls;
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
        return walls;
    }

    private static List<Point2d> createRenderablePolygon(List<Pair<Point2d,Point2d>> walls, Room room){
        List<Point2d> p = new ArrayList<>();
        Pair<Point2d, Point2d> firstPair = null;
        Pair<Point2d, Point2d> lastPair = null;

        int panic = 0;
        while((panic ++) != walls.size() * 2 + 1){
            if(firstPair == null){
                firstPair = walls.get(0);
                lastPair = firstPair;
                p.add(new Point2d(((firstPair.fst.x)),
                        (int)((firstPair.fst.y) )));
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
            p.add(new Point2d(((current.fst.x)),
                    (int)((current.fst.y))));
            lastPair = current;
        }
        if(panic == walls.size() *2 +1){
            System.err.println("Room: " + room.getName() + "is invalid");
            return null;
        }
        return p;
    }

    public static boolean linesIntersect(Point2d p1, Point2d p2, Point2d p3, Point2d p4){

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

    private static List<Pair<Point2d,Point2d>> calculateRectangleEdgePairs(double x, double y, double width, double height){
        java.util.List<Pair<Point2d,Point2d>> points = new ArrayList<>();
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



}
