// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: school.proto

package uc.mei.is.ProtocolBufferClasses;

public final class ProtocolBufferClasses {
  private ProtocolBufferClasses() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_model_ProtoProfessor_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_model_ProtoProfessor_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_model_ProtoStudent_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_model_ProtoStudent_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_model_ProtoSchool_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_model_ProtoSchool_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\014school.proto\022\005model\"s\n\016ProtoProfessor\022" +
      "\n\n\002id\030\001 \002(\t\022\014\n\004name\030\002 \002(\t\022\021\n\tbirthDate\030\004" +
      " \002(\t\022\021\n\ttelephone\030\003 \002(\t\022\017\n\007address\030\005 \002(\t" +
      "\022\020\n\010students\030\006 \003(\t\"\234\001\n\014ProtoStudent\022\n\n\002i" +
      "d\030\001 \002(\t\022\014\n\004name\030\002 \002(\t\022\021\n\ttelephone\030\003 \002(\t" +
      "\022\016\n\006gender\030\004 \002(\t\022\021\n\tbirthDate\030\005 \002(\t\022\030\n\020r" +
      "egistrationDate\030\006 \002(\t\022\017\n\007address\030\007 \002(\t\022\021" +
      "\n\tprofessor\030\010 \001(\t\"_\n\013ProtoSchool\022)\n\nprof" +
      "essors\030\001 \003(\0132\025.model.ProtoProfessor\022%\n\010s" +
      "tudents\030\002 \003(\0132\023.model.ProtoStudentB:\n\037uc" +
      ".mei.is.ProtocolBufferClassesB\025ProtocolB" +
      "ufferClassesP\001"
    };
    descriptor = com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
        });
    internal_static_model_ProtoProfessor_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_model_ProtoProfessor_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_model_ProtoProfessor_descriptor,
        new java.lang.String[] { "Id", "Name", "BirthDate", "Telephone", "Address", "Students", });
    internal_static_model_ProtoStudent_descriptor =
      getDescriptor().getMessageTypes().get(1);
    internal_static_model_ProtoStudent_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_model_ProtoStudent_descriptor,
        new java.lang.String[] { "Id", "Name", "Telephone", "Gender", "BirthDate", "RegistrationDate", "Address", "Professor", });
    internal_static_model_ProtoSchool_descriptor =
      getDescriptor().getMessageTypes().get(2);
    internal_static_model_ProtoSchool_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_model_ProtoSchool_descriptor,
        new java.lang.String[] { "Professors", "Students", });
  }

  // @@protoc_insertion_point(outer_class_scope)
}
