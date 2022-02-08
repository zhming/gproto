# --*-- coding: utf-8 --*--

import base64
from dataclasses import field
from pydoc import describe
from google.protobuf import json_format
from google.protobuf import reflection as _reflection
from google.protobuf import type_pb2

from typing import get_type_hints, List, Dict
import json

# protobuf base64数据转json字符串
#   model_pb2_name    protobuf生成的pb2.py文件名称
#   func_proto_object_name        proto定义对象名称
#   json_data   json格式数据
def proto_to_Json(model_pb2_name, func_proto_object_name, protobuf_base64):
    # base64解码成二进制数据
    protobufBytes = base64.b64decode(protobuf_base64).decode("utf-8")

    # 动态导入proto_pb2文件
    model = __import__(model_pb2_name)

    # 反射获取proto object 方法
    func_proto_object = getattr(model, func_proto_object_name)

    # 通过反射获取message对象
    message = func_proto_object()

    # 解析二进制数据为proto_message对象
    message.ParseFromString(protobufBytes)

    # proto message 转 json 数据
    jsonResult = json_format.MessageToJson(message, True, True, 0, True, False, None, 2)

    return jsonResult

# JSON数据转protobuf
#   model_pb2_name    protobuf生成的pb2.py文件名称
#   func_proto_object_name        proto定义对象名称
#   json_data   json格式数据
def json_to_Proto(model_pb2_name, func_proto_object_name, json_data):
    # 导入proto_pb model
    model = __import__(model_pb2_name)

    # 反射，获取message对象方法
    function_proto_object = getattr(model, func_proto_object_name)

    # 获取proto message对象
    message = function_proto_object()

    # json转proto message
    json_format.Parse(json.dumps(json_data), message, True)
    
    # 生成二进制数据
    tempProtoBytes = message.SerializeToString()

    #base64编码
    protoBase64 = base64.b64encode(tempProtoBytes)  # 被编码的参数必须是二进制数据

    return protoBase64

# 获取指定field默认json数据示例
# JSON数据转protobuf
#   model_pb2_name    protobuf生成的pb2.py文件名称
#   func_proto_object_name        proto定义对象名称
#   json_data   json格式数据
def get_default_json(model_pb2_name, func_proto_object_name, field_name):
    # 导入proto_pb model
    model = __import__(model_pb2_name)

    # 反射，获取message对象方法
    function_proto_object = getattr(model, func_proto_object_name)

    # 获取proto message对象
    message = function_proto_object()

    message_field = getattr(message, field_name)
    

    func_obj = getattr(model, "_MESSAGEENVELOPE")

   
    default_value = func_obj.fields_by_name[field_name].default_value
    field_type = func_obj.fields_by_name[field_name].type
    field_message_type = func_obj.fields_by_name[field_name].message_type

    if None == field_message_type: 
        if [] == default_value:
            jsonResult = processList(model, func_obj, field_name)
        else:
            jsonResult = getDefaultValue(field_type)
    else:
        if [] == default_value: 
            jsonResult = processList(model, func_obj, field_name)
        else:     
            jsonResult = json_format.MessageToJson(message_field, True, True, 0, True, False, None, 2)
            jsonResult = json.dumps(json.loads(jsonResult))

    return jsonResult

def processList(model, func_obj, field_name):
    jsonResult = ""
    field_type = func_obj.fields_by_name[field_name].message_type
    if  field_type == None :
        default_value = func_obj.fields_by_name[field_name].default_value
        field_type = func_obj.fields_by_name[field_name].type

        if [] == default_value:
            default_value_by_type = getDefaultValue(field_type)
            strList = [default_value_by_type]
            # jsonResult = "[" + default_value_by_type +"]"
            jsonResult = json.dumps(strList)
    else:
        field_type_name = func_obj.fields_by_name[field_name].message_type.name
        message_field = getattr(model, field_type_name)
        jsonTemp = json_format.MessageToJson(message_field(), True, True, 2, True, False, None, 2)
        strList = [json.loads(jsonTemp)]
        jsonResult = json.dumps(strList)
    return jsonResult


protobufTypedict = {
    0: "unknow",
    1: 1.0,
    2: 1.0,
    3: 0,
    4: 0,
    # TYPE_INT32 = Field.Kind.V(5)
    #    """Field type int32."""
    5: 0,
    #    TYPE_FIXED64 = Field.Kind.V(6)
    #    """Field type fixed64."""
    6: 0,
     #   TYPE_FIXED32 = Field.Kind.V(7)
     #   """Field type fixed32."""
    7: 0,
     #   TYPE_BOOL = Field.Kind.V(8)
     #   """Field type bool."""
    8: True,
    #    TYPE_STRING = Field.Kind.V(9)
    #    """Field type string."""
    9: "string value",
    #    TYPE_GROUP = Field.Kind.V(10)
    #    """Field type group. Proto2 syntax only, and deprecated."""
    10: "group",
    #    TYPE_MESSAGE = Field.Kind.V(11)
    #    """Field type message."""
    11: 0,
    #    TYPE_BYTES = Field.Kind.V(12)
    #    """Field type bytes."""
    12: "bytes",
    #    TYPE_UINT32 = Field.Kind.V(13)
    #    """Field type uint32."""
    13: 1,
     #   TYPE_ENUM = Field.Kind.V(14)
     #   """Field type enum."""
    14: "enum",
     #   TYPE_SFIXED32 = Field.Kind.V(15)
     #   """Field type sfixed32."""
    16: 0,
     #   TYPE_SFIXED64 = Field.Kind.V(16)
      #  """Field type sfixed64."""
    17: 0,
     #   TYPE_SINT32 = Field.Kind.V(17)
     #   """Field type sint32."""
    18: 0
     #   TYPE_SINT64 = Field.Kind.V(18)
     #   """Field type sint64."""
}

def getDefaultValue(typeNum):
    return protobufTypedict.get(typeNum, "Unknow Protobuf Type")

def main() :
    # bug Players error
    print(get_default_json("message_pb2", "MessageEnvelope", "TargetId"))
    print(get_default_json("message_pb2", "MessageEnvelope", "user"))

    print(get_default_json("message_pb2", "MessageEnvelope", "Players"))
    print(get_default_json("message_pb2", "MessageEnvelope", "list"))
    print(get_default_json("message_pb2", "MessageEnvelope", "intList"))

if __name__ == '__main__':
    main()