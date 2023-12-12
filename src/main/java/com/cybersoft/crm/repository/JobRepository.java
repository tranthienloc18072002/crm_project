package com.cybersoft.crm.repository;

import com.cybersoft.crm.config.MysqlConnection;
import com.cybersoft.crm.entity.JobEntity;
import com.cybersoft.crm.entity.TaskEntity;
import com.cybersoft.crm.entity.UserEntity;
import com.cybersoft.crm.model.JobDetailsModel;
import com.cybersoft.crm.model.UserModel;
import com.cybersoft.crm.service.TaskService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JobRepository {

    public List<JobEntity> getJobs() {
        List<JobEntity> jobs = new ArrayList<>();
        try {
            Connection connection = MysqlConnection.getConnection();
            String query = "select * from jobs";
            PreparedStatement ps = connection.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                JobEntity job = new JobEntity();
                job.setId(rs.getInt("id"));
                job.setName(rs.getString("name"));
                job.setStartDate(rs.getString("start_date"));
                job.setEndDate(rs.getString("end_date"));
                jobs.add(job);
            }


            connection.close();
        } catch (SQLException e) {
            System.out.println("Error get jobs " + e.getMessage());
        }
        return jobs;
    }

    public int deleteJobById(int id) {
        int result = 0;
        try {
            Connection connection = MysqlConnection.getConnection();
            String query = "delete from jobs j where j.id = ?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, id);
            result = ps.executeUpdate();

            connection.close();
        } catch (SQLException e) {
            System.out.println("Error delete job " + e.getMessage());
        }
        return result;
    }

    public int insertJob(JobEntity job) {
        int result = 0;
        try {
            Connection connection = MysqlConnection.getConnection();
            String query = "insert into jobs (name, start_date, end_date) values (?, ?, ?);";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, job.getName());
            ps.setString(2, job.getStartDate());
            ps.setString(3, job.getEndDate());
            result = ps.executeUpdate();

            connection.close();
        } catch (SQLException e) {
            System.out.println("Error insert job " + e.getMessage());
        }
        return result;
    }

    public JobEntity findJobById(int id) {
        JobEntity job = new JobEntity();
        try{
            String query = "select * from jobs where id = ?";
            Connection connection = MysqlConnection.getConnection();
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1,id);

            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                job.setId(rs.getInt("id"));
                job.setName(rs.getString("name"));
                job.setStartDate(rs.getString("start_date"));
                job.setEndDate(rs.getString("end_date"));
            }

            connection.close();

        }catch (Exception e){
            System.out.println("Error findJobById " + e.getMessage());
        }

        return job;
    }

    public int updateJob(JobEntity job) {
        int result = 0;
        try {
            Connection connection = MysqlConnection.getConnection();
            String query = "update jobs set name=?, start_date=?, end_date=? where id=?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, job.getName());
            ps.setString(2, job.getStartDate());
            ps.setString(3, job.getEndDate());
            ps.setInt(4, job.getId());
            result = ps.executeUpdate();

            connection.close();
        } catch (SQLException e) {
            System.out.println("Error update job " + e.getMessage());
        }
        return result;
    }

    public List<UserEntity> getUserByJobId(int jobId) {
        List<UserEntity> users = new ArrayList<>();
        try {
            Connection connection = MysqlConnection.getConnection();
            String query = "select distinct user_id, firstname, lastname\n" +
                    "from users u inner join tasks t\n" +
                    "on u.id = t.user_id and t.job_id = ?;";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, jobId);
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                UserEntity user = new UserEntity();
                user.setId(rs.getInt("user_id"));
                user.setFirstName(rs.getString("firstname"));
                user.setLastName(rs.getString("lastname"));
                users.add(user);
            }


            connection.close();
        } catch (SQLException e) {
            System.out.println("Error get users by jobId " + e.getMessage());
        }
        return users;
    }

    public List<TaskEntity> getTaskByJobIdAndUserId(int jobId, int userId) {
        List<TaskEntity> tasks = new ArrayList<>();
        try {
            Connection connection = MysqlConnection.getConnection();
            String query = "select * from tasks t\n" +
                    "where t.user_id = ? and t.job_id = ?;";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, userId);
            ps.setInt(2, jobId);
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                TaskEntity task = new TaskEntity();
                task.setId(rs.getInt("id"));
                task.setName(rs.getString("name"));
                task.setStartDate(rs.getString("start_date"));
                task.setEndDate(rs.getString("end_date"));
                task.setStatusId(rs.getInt("status_id"));
                tasks.add(task);
            }


            connection.close();
        } catch (SQLException e) {
            System.out.println("Error get tasks by jobId and userId " + e.getMessage());
        }
        return tasks;
    }

}
