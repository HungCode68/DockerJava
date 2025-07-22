package controller;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.*;
import java.util.*;

import model.SinhVien;
import utils.DBUtil;

@WebServlet(name = "SinhVienServlet", urlPatterns = {"/sinhVien"})
public class SinhVienServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        List<SinhVien> list = new ArrayList<>();

        try (Connection conn = DBUtil.getConnection()) {
            String sql = "SELECT * FROM SinhVien";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                SinhVien sv = new SinhVien();
                sv.setMaSV(rs.getString("MaSV"));
                sv.setHoTen(rs.getString("HoTen"));
                sv.setNamSinh(rs.getInt("NamSinh"));
                sv.setLop(rs.getString("Lop"));

                list.add(sv);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        request.setAttribute("dsSinhVien", list);
        request.getRequestDispatcher("sinhvien.jsp").forward(request, response);
    }
}
