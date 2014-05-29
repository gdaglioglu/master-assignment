/**
 * @(#)DataClassTest.java    1.0 05/11/2008
 *
 * Candidate: Roberto Perillo
 * Prometric ID: Your Prometric ID here
 * Candidate ID: Your candidade ID here
 *
 * Sun Certified Developer for Java 2 Platform, Standard Edition Programming
 * Assignment (CX-310-252A)
 *
 * This class is part of the Programming Assignment of the Sun Certified
 * Developer for Java 2 Platform, Standard Edition certification program, must
 * not be used out of this context and must be used exclusively by Sun
 * Microsystems, Inc.
 */
package suncertify.db;

import suncertify.model.HotelRoom;

/**
 * The <code>DataClassTest</code> tests the main functionalities of the
 * {@link Data} class. In order to simulate several clients trying to use it and
 * exercise the locking mechanism, it also has several inner classes that extend
 * the {@link Thread} class, where each class represents one client requesting
 * one operation, and mainly requesting updating and deletion of records. The
 * <code>FindingRecordsThread</code> exercises two functionalities: finding
 * records and reading records.
 *
 * @author Roberto Perillo
 * @version 1.0 05/11/2008
 *
 * @author Luke GJ Potter
 * @since 29/05/2014
 */
public class LockingManagerTest {

    private static final Data data = new Data();

    public static void main(String [] args) {
        new LockingManagerTest().startTests();
    }

    public void startTests() {
        try {

            /*
             * Practically, it is not necessary to execute this loop more than 1
             * time, but if you want, you can increase the controller variable,
             * so it is executed as many times as you want
             */
            for (int i = 0; i < 10; i++) {
                Thread updatingRandom = new UpdatingRandomRecordThread();
                updatingRandom.start();
                Thread updatingRecord1 = new UpdatingRecord1Thread();
                updatingRecord1.start();
                Thread creatingRecord = new CreatingRecordThread();
                creatingRecord.start();
                Thread deletingRecord = new DeletingRecord1Thread();
                deletingRecord.start();
                Thread findingRecords = new FindingRecordsThread();
                findingRecords.start();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    private class UpdatingRandomRecordThread extends Thread {

        @SuppressWarnings("deprecation")
        public void run() {
            final HotelRoom room = new HotelRoom();
            room.setName("Palace");
            room.setLocation("Smallville");
            room.setRoomSize(2);
            room.setSmoking(true);
            room.setRate(150.00);
            room.setDate("2014/06/02");
            room.setOwnerName("54120584");

            final int recNo = (int) (Math.random() * 50);
            try {
                System.out.println(Thread.currentThread().getId()
                        + " trying to lock record #" + recNo
                        + " on UpdatingRandomRecordThread");

                /*
                 * The generated record number may not exist in the database, so
                 * a RecordNotFoundException must be thrown by the lock method.
                 * Since the database records are in a cache, it is not
                 * necessary to put the unlock instruction in a finally block,
                 * because an exception can only occur when calling the lock
                 * method (not when calling the update/delete methods),
                 * therefore it is not necessary to call the unlock method in a
                 * finally block, but you can customize this code according to
                 * your reality
                 */
                //long cookie = data.lockRecord(recNo);
                System.out.println(Thread.currentThread().getId()
                        + " trying to update record #" + recNo
                        + " on UpdatingRandomRecordThread");

                /*
                 * An exception cannot occur here, otherwise, the unlock
                 * instruction will not be reached, and the record will be
                 * locked forever. In this case, I created a class called
                 * RoomRetriever, which transforms from Room to String array,
                 * and vice-versa, but it could also be done this way:
                 *
                 * data.update(recNo, new String[] {"Palace", "Smallville", "2",
                 * "Y", "$150.00", "2005/07/27", null});
                 */
                data.updateRecord(recNo, room.toStringArray(), Thread.currentThread().getId());
                System.out.println(Thread.currentThread().getId()
                        + " trying to unlock record #" + recNo
                        + " on UpdatingRandomRecordThread");
                //data.unlock(recNo, cookie);
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    private class UpdatingRecord1Thread extends Thread {

        @SuppressWarnings("deprecation")
        public void run() {
            final HotelRoom room = new HotelRoom();
            room.setName("Castle");
            room.setLocation("Digitopolis");
            room.setRoomSize(2);
            room.setSmoking(false);
            room.setRate(90.00);
            room.setDate("2014/06/01");
            room.setOwnerName("88006644");

            try {
                System.out.println(Thread.currentThread().getId()
                        + " trying to lock record #1 on"
                        + " UpdatingRecord1Thread");
                //long cookie = data.lockRecord(1);
                System.out.println(Thread.currentThread().getId()
                        + " trying to update record #1 on"
                        + " UpdatingRecord1Thread");
                data.updateRecord(1, room.toStringArray(), Thread.currentThread().getId());
                System.out.println(Thread.currentThread().getId()
                        + " trying to unlock record #1 on"
                        + "UpdatingRecord1Thread");

                /*
                 * In order to see the deadlock, this instruction can be
                 * commented, and the other Threads, waiting to update/delete
                 * record #1 will wait forever and the deadlock will occur
                 */
                //data.unlock(1, cookie);
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    private class CreatingRecordThread extends Thread {

        @SuppressWarnings("deprecation")
        public void run() {
            HotelRoom room = new HotelRoom();
            room.setName("Elephant Inn");
            room.setLocation("EmeraldCity");
            room.setRoomSize(6);
            room.setSmoking(false);
            room.setRate(120.00);
            room.setDate("2014/06/01");

            try {
                System.out.println(Thread.currentThread().getId()
                        + " trying to create a record");
                data.createRecord(room.toStringArray());
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    private class DeletingRecord1Thread extends Thread {

        public void run() {
            try {
                System.out.println(Thread.currentThread().getId()
                        + " trying to lock record #1 on "
                        + "DeletingRecord1Thread");
                //long cookie = data.lockRecord(1);
                System.out.println(Thread.currentThread().getId()
                        + " trying to delete record #1 on "
                        + "DeletingRecord1Thread");
                data.deleteRecord(1, Thread.currentThread().getId());
                System.out.println(Thread.currentThread().getId()
                        + " trying to unlock record #1 on "
                        + "DeletingRecord1Thread");
                //data.unlock(1, cookie);
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    private class FindingRecordsThread extends Thread {

        public void run() {
            try {
                System.out.println(Thread.currentThread().getId()
                        + " trying to find records");
                final String [] criteria = {"Palace", "Smallville"};
                final long [] results = data.findByCriteria(criteria);

                for (int i = 0; i < results.length; i++) {
                    System.out.println(results.length + " results found.");
                    try {
                        final String message = Thread.currentThread().getId()
                                + " going to read record #" + results[i]
                                + " in FindingRecordsThread - still "
                                + ((results.length - 1) - i) + " to go.";
                        System.out.println(message);
                        final String[] room = data.readRecord(results[i]);
                        System.out.println("Hotel (FindingRecordsThread): "
                                + room[0]);
                        System.out.println("Has next? "
                                + (i < (results.length - 1)));
                    } catch (Exception e) {
                        /*
                         * In case a record was found during the execution of
                         * the find method, but deleted before the execution of
                         * the read instruction, a RecordNotFoundException will
                         * occur, which would be normal then
                         */
                        System.out.println("Exception in "
                                + "FindingRecordsThread - " + e);
                    }
                }
                System.out.println("Exiting for loop");
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }
}
