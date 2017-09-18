package pl.workreporter.web.controllers.employee;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.workreporter.web.beans.security.rest.RestResponse;
import pl.workreporter.web.beans.security.rest.RestResponseSuccess;
import pl.workreporter.web.service.mail.MailNotifier;
import pl.workreporter.web.service.password.generator.PasswordGenerator;
import pl.workreporter.web.beans.entities.user.User;
import pl.workreporter.web.beans.entities.user.UserDao;

import java.util.List;
import java.util.Map;

import static org.springframework.web.bind.annotation.RequestMethod.*;

/**
 * Created by Sergiusz on 19.08.2017.
 */
@RestController
public class EmployeeRestController {
    @Autowired
    private UserDao userDao;
    @Autowired
    private PasswordGenerator passwordGenerator;
    @Autowired
    private MailNotifier notifier;

    @RequestMapping(value = "/solution/employees", params = "id",  method = GET)
    public @ResponseBody
    RestResponse<List<User>> getAllEmployees(@RequestParam("id") long solutionId) {
        List<User> result = userDao.getAllUsersInSolution(solutionId);
        return new RestResponseSuccess<>(result);
    }

    @RequestMapping(value = "/solution/employees", params = "teamid", method = GET)
    public @ResponseBody
    RestResponse<List<User>> getAllEmployeesInTeam(@RequestParam("teamid") long teamId) {
        List<User> result = userDao.getAllUsersInTeam(teamId);
        return new RestResponseSuccess<>(result);
    }

    @RequestMapping(value = "/solution/employees/{userid}", method = DELETE)
    public RestResponse<Void> removeEmployee(@RequestParam("solutionid") long solutionId,
                               @PathVariable("userid") long employeeId) {
        userDao.removeUser(solutionId, employeeId);
        return new RestResponseSuccess<>();
    }

    @RequestMapping(value = "/solution/employees", method = DELETE)
    public RestResponse<Void> removeSelectedEmployees(@RequestParam("solutionid") long solutionId,
                                        @RequestParam("employees") List<Long> employees) {
        userDao.removeUsers(solutionId, employees);
        return new RestResponseSuccess<>();
    }

    @RequestMapping(value = "/solution/employees/{id}", method = PATCH)
    public RestResponse<User> updateEmployee(@PathVariable("id") long employeeId, @RequestBody Map<String, String> employee) {
        return new RestResponseSuccess<>(userDao.updateUser(employeeId, employee));
    }

    @RequestMapping(value="/solution/employees", method = POST)
    public RestResponse<User> addEmployee(@RequestBody Map<String, String> employee) {
        String password = passwordGenerator.generate(12);
        Long teamId = employee.get("teamid").isEmpty() ? null : Long.parseLong(employee.get("teamid"));
        String birthday = employee.get("birthday").isEmpty() ? null : employee.get("birthday");
        String phone = employee.get("phone").isEmpty() ? null : employee.get("phone");
        User user = userDao.addUser(Long.parseLong(employee.get("solutionid")), teamId,
                Long.parseLong(employee.get("positionid")), Double.parseDouble(employee.get("workingtime")),
                employee.get("firstname"), employee.get("lastname"), birthday, phone,
                employee.get("login"), password, employee.get("email"));
        notifier.sendInitialMessage(employee.get("login"), password, employee.get("email"));
        return new RestResponseSuccess<>(user);
    }
}
