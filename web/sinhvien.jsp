<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.*, model.SinhVien" %>

<html>
<head>
    <title>Danh sách sinh viên</title>
</head>
<body>
<h2>Danh sách sinh viên</h2>
<table border="1" cellpadding="8">
    <tr>
        <th>Mã SV</th>
        <th>Họ Tên</th>
        <th>Năm Sinh</th>
        <th>Lớp</th>
    </tr>

<%
    List<SinhVien> ds = (List<SinhVien>) request.getAttribute("dsSinhVien");
    if (ds != null && !ds.isEmpty()) {
        for (SinhVien sv : ds) {
%>
    <tr>
        <td><%= sv.getMaSV() %></td>
        <td><%= sv.getHoTen() %></td>
        <td><%= sv.getNamSinh() %></td>
        <td><%= sv.getLop() %></td>
    </tr>
<%
        }
    } else {
%>
    <tr><td colspan="4">Không có dữ liệu sinh viên.</td></tr>
<%
    }
%>
</table>
</body>
</html>
