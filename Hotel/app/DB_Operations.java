import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DB_Operations extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "HotelBookingDB";
    private static final int DATABASE_VERSION = 1;

    public DB_Operations(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // User table
        sqLiteDatabase.execSQL("CREATE TABLE tblUser(Username VARCHAR(20) PRIMARY KEY, Mobile INTEGER, Address VARCHAR, Password INTEGER)");

        // Room table
        sqLiteDatabase.execSQL("CREATE TABLE tblRoom(RoomID INTEGER PRIMARY KEY, RoomType VARCHAR(20), PricePerNight DOUBLE, Image BLOB, Description VARCHAR(100))");

        // Reservation table for room bookings
        sqLiteDatabase.execSQL("CREATE TABLE tblReservation(ReservationID INTEGER PRIMARY KEY AUTOINCREMENT, Username VARCHAR(20), RoomID INTEGER, CheckInDate TEXT, CheckOutDate TEXT, TotalPrice DOUBLE, FOREIGN KEY(Username) REFERENCES tblUser(Username), FOREIGN KEY(RoomID) REFERENCES tblRoom(RoomID))");

        // Service table
        sqLiteDatabase.execSQL("CREATE TABLE tblService(ServiceID INTEGER PRIMARY KEY, ServiceName VARCHAR(30), Description VARCHAR(100), Price DOUBLE, Image BLOB)");

        // Service reservation table
        sqLiteDatabase.execSQL("CREATE TABLE tblServiceReservation(ReservationID INTEGER PRIMARY KEY AUTOINCREMENT, Username VARCHAR(20), ServiceID INTEGER, ReservationDate TEXT, ReservationTime TEXT, FOREIGN KEY(Username) REFERENCES tblUser(Username), FOREIGN KEY(ServiceID) REFERENCES tblService(ServiceID))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS tblUser");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS tblRoom");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS tblReservation");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS tblService");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS tblServiceReservation");
        onCreate(sqLiteDatabase);
    }

    // User creation method
    public void createUser(User user) {
        ContentValues values = new ContentValues();
        values.put("Username", user.getUsername());
        values.put("Mobile", user.getMobile());
        values.put("Address", user.getAddress());
        values.put("Password", user.getPassword());
        SQLiteDatabase database = getWritableDatabase();
        database.insert("tblUser", null, values);
        database.close();
    }

    // Method to add a new room
    public void addRoom(Room room) {
        ContentValues values = new ContentValues();
        values.put("RoomID", room.getRoomID());
        values.put("RoomType", room.getRoomType());
        values.put("PricePerNight", room.getPricePerNight());
        values.put("Image", room.getImage());
        values.put("Description", room.getDescription());
        SQLiteDatabase database = getWritableDatabase();
        database.insert("tblRoom", null, values);
        database.close();
    }

    // Method to add a new service
    public void addService(Service service) {
        ContentValues values = new ContentValues();
        values.put("ServiceID", service.getServiceID());
        values.put("ServiceName", service.getServiceName());
        values.put("Description", service.getDescription());
        values.put("Price", service.getPrice());
        values.put("Image", service.getImage());
        SQLiteDatabase database = getWritableDatabase();
        database.insert("tblService", null, values);
        database.close();
    }

    // Method to reserve a room
    public void reserveRoom(Reservation reservation) {
        ContentValues values = new ContentValues();
        values.put("Username", reservation.getUsername());
        values.put("RoomID", reservation.getRoomID());
        values.put("CheckInDate", reservation.getCheckInDate());
        values.put("CheckOutDate", reservation.getCheckOutDate());
        values.put("TotalPrice", reservation.getTotalPrice());
        SQLiteDatabase database = getWritableDatabase();
        database.insert("tblReservation", null, values);
        database.close();
    }

    // Method to reserve a service
    public void reserveService(ServiceReservation serviceReservation) {
        ContentValues values = new ContentValues();
        values.put("Username", serviceReservation.getUsername());
        values.put("ServiceID", serviceReservation.getServiceID());
        values.put("ReservationDate", serviceReservation.getReservationDate());
        values.put("ReservationTime", serviceReservation.getReservationTime());
        SQLiteDatabase database = getWritableDatabase();
        database.insert("tblServiceReservation", null, values);
        database.close();
    }

    // Method to view all services
    public ArrayList<Service> viewAllServices() {
        ArrayList<Service> servicesList = new ArrayList<>();
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM tblService", null);

        if (cursor.moveToFirst()) {
            do {
                Service service = new Service();
                service.setServiceID(cursor.getInt(0));
                service.setServiceName(cursor.getString(1));
                service.setDescription(cursor.getString(2));
                service.setPrice(cursor.getDouble(3));
                service.setImage(cursor.getBlob(4));
                servicesList.add(service);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return servicesList;
    }

    // Method to view all room types
    public ArrayList<Room> viewAllRooms() {
        ArrayList<Room> roomList = new ArrayList<>();
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM tblRoom", null);

        if (cursor.moveToFirst()) {
            do {
                Room room = new Room();
                room.setRoomID(cursor.getInt(0));
                room.setRoomType(cursor.getString(1));
                room.setPricePerNight(cursor.getDouble(2));
                room.setImage(cursor.getBlob(3));
                room.setDescription(cursor.getString(4));
                roomList.add(room);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return roomList;
    }
}
