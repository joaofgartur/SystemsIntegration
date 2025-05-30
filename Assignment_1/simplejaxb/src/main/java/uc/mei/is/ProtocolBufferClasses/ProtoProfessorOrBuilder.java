// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: school.proto

package uc.mei.is.ProtocolBufferClasses;

public interface ProtoProfessorOrBuilder extends
    // @@protoc_insertion_point(interface_extends:model.ProtoProfessor)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>required string id = 1;</code>
   * @return Whether the id field is set.
   */
  boolean hasId();
  /**
   * <code>required string id = 1;</code>
   * @return The id.
   */
  java.lang.String getId();
  /**
   * <code>required string id = 1;</code>
   * @return The bytes for id.
   */
  com.google.protobuf.ByteString
      getIdBytes();

  /**
   * <code>required string name = 2;</code>
   * @return Whether the name field is set.
   */
  boolean hasName();
  /**
   * <code>required string name = 2;</code>
   * @return The name.
   */
  java.lang.String getName();
  /**
   * <code>required string name = 2;</code>
   * @return The bytes for name.
   */
  com.google.protobuf.ByteString
      getNameBytes();

  /**
   * <code>required string birthDate = 4;</code>
   * @return Whether the birthDate field is set.
   */
  boolean hasBirthDate();
  /**
   * <code>required string birthDate = 4;</code>
   * @return The birthDate.
   */
  java.lang.String getBirthDate();
  /**
   * <code>required string birthDate = 4;</code>
   * @return The bytes for birthDate.
   */
  com.google.protobuf.ByteString
      getBirthDateBytes();

  /**
   * <code>required string telephone = 3;</code>
   * @return Whether the telephone field is set.
   */
  boolean hasTelephone();
  /**
   * <code>required string telephone = 3;</code>
   * @return The telephone.
   */
  java.lang.String getTelephone();
  /**
   * <code>required string telephone = 3;</code>
   * @return The bytes for telephone.
   */
  com.google.protobuf.ByteString
      getTelephoneBytes();

  /**
   * <code>required string address = 5;</code>
   * @return Whether the address field is set.
   */
  boolean hasAddress();
  /**
   * <code>required string address = 5;</code>
   * @return The address.
   */
  java.lang.String getAddress();
  /**
   * <code>required string address = 5;</code>
   * @return The bytes for address.
   */
  com.google.protobuf.ByteString
      getAddressBytes();

  /**
   * <code>repeated string students = 6;</code>
   * @return A list containing the students.
   */
  java.util.List<java.lang.String>
      getStudentsList();
  /**
   * <code>repeated string students = 6;</code>
   * @return The count of students.
   */
  int getStudentsCount();
  /**
   * <code>repeated string students = 6;</code>
   * @param index The index of the element to return.
   * @return The students at the given index.
   */
  java.lang.String getStudents(int index);
  /**
   * <code>repeated string students = 6;</code>
   * @param index The index of the value to return.
   * @return The bytes of the students at the given index.
   */
  com.google.protobuf.ByteString
      getStudentsBytes(int index);
}
