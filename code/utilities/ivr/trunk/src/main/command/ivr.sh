#!/bin/bash

global_pid=0
pid_file_path="/var/run/ivr.pid"
base_path="/opt/inov8"
ivr_path="$base_path/ivr"
ivr_res_path="$ivr_path/resources"
ivr_lib_path="$ivr_path/lib"
log4j_conf_path="$ivr_res_path/log4j.properties"
dummy_cmd_name="inov8.ivr"

app_run_cmd="java -cp \".:$ivr_path/*:$ivr_res_path/:$ivr_lib_path/*\" -Dlog4j.configuration=\"file:$log4j_conf_path\" -D$dummy_cmd_name=\"$dummy_cmd_name\" com.inov8.Main"


function locate_ivr_process()
{
   local ret=$1
   local token=$2
   local pid
   local status=0

   echo "Attempting to locate running IVR process"
   pid=$(pgrep -f $dummy_cmd_name)
   if [ ! "$pid" = "" ]; then
      if [ "$pid" -gt 0 ]; then
         echo "Running IVR process found"
         echo "$pid" > "$pid_file_path"
         status=1
      fi
   fi

   global_pid=$pid
   eval $ret="'$status'"
}

function process_exist()
{
   local status=$1
   local pid=$2
   local pname=$(ps -p $pid -o args | grep $dummy_cmd_name)

   if [ "$pname" = "" ]; then
      pname=0
   else
      pname=1
   fi

   eval $status="'$pname'"
}

function start()
{
   local ret=$1
   local status=$2
   local pid=$3
   local result

   if [ "$status" -eq 1 ]; then
      echo "IVR already running. PID: $pid"
   else
      echo "Attempting to start..."
      eval "($app_run_cmd) &"
      pid=$!
      sleep 2
      process_exist result $pid
      if [ "$result" -eq 1 ]; then
         echo "$pid" > "$pid_file_path"
         echo "IVR started with PID $pid"
         status=1
      else
         echo "IVR start failed."
      fi
   fi

   eval $ret="'$status'"
}

function stop()
{
   local ret=$1
   local status=$2
   local pid=$3

   if [ "$status" -eq 0 ]; then
      echo "IVR already stopped"
      status=1
   else
      status=0
      echo "Attempting to stop"
      kill -15 $pid

      local count=0
      while [ "$status" -eq 1 ]
      do
         if [ "$count" -eq 10 ]; then
            kill -9 $pid
         fi
         echo "."
         sleep 1
         process_exist status $pid
         count=$((count+1))
         if [ "$count" -eq 20 ]; then
            status=-1
            echo "Unable to stop IVR process. PID: $pid"
         fi
      done
      if [ "$status" -gt -1 ]; then
         rm -f "$pid_file_path"
         echo "IVR stopped"
         status=1
      fi
   fi
   
   eval $ret="'$status'"
}

function main()
{
   local cmd=$1
   local status=-1
   local pid
   local result

   if [ "$#" -ne 1 ]; then
      cmd="dummmy"
      status=0
   else
      if [ -f "$pid_file_path" ]; then
         pid=$(cat "$pid_file_path")
         if [ "$pid" = "" ]; then
            rm -f "$pid_file_path"
            echo "Valid process not found"
            locate_ivr_process result $dummy_cmd_name
            status=$result
            pid=$global_pid
         else
            process_exist result $pid
            status=$result
            if [ "$status" -eq 0 ]; then
               rm -f "$pid_file_path"
               echo "Valid process not found"
               locate_ivr_process result $dummy_cmd_name
               status=$result
               pid=$global_pid
            fi
         fi
      else
         locate_ivr_process result $dummy_cmd_name
         status=$result
         pid=$global_pid
      fi
   fi

   if [ "$status" -gt -1 ]; then
      case $cmd in
         start)
            start result $status $pid
         ;;
         stop)
            stop result $status $pid
         ;;
         restart)
           stop result $status $pid

           if [ "$result" -eq 1 ]; then
              start result 0 0
           fi
         ;;
         status)
            if [ "$status" -eq 0 ]; then
               echo "IVR is not running"
            else
               echo "IVR is running. PID: $pid"
            fi
         ;;
         *)
            echo "Usage: Valid arguments are status, start, stop or restart"
      esac
   fi
}

main $*
