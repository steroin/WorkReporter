package pl.workreporter.web.controllers.employee;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.workreporter.web.service.mail.MailNotificator;
import pl.workreporter.web.service.pwdgen.PasswordGenerator;
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
    private MailNotificator notificator;

    @RequestMapping(value = "/solution/employees", params = "id",  method = GET)
    public @ResponseBody
    List<User> getAllEmployees(@RequestParam("id") long solutionId) {
        List<User> result = userDao.getAllUsersInSolution(solutionId);
        return result;
    }

    @RequestMapping(value = "/solution/employees", params = "teamid", method = GET)
    public @ResponseBody
    List<User> getAllEmployeesInTeam(@RequestParam("teamid") long teamId) {
        List<User> result = userDao.getAllUsersInTeam(teamId);
        return result;
    }

    @RequestMapping(value = "/solution/employees/{userid}", method = DELETE)
    public void removeEmployee(@RequestParam("solutionid") long solutionId,
                               @RequestParam("personaldataid") long personalDataId,
                               @RequestParam("accountid") long accountId,
                               @PathVariable("userid") long employeeId) {
        userDao.removeUser(solutionId, employeeId, personalDataId, accountId);
    }

    @RequestMapping(value = "/solution/employees", method = DELETE)
    public void removeSelectedEmployees(@RequestParam("solutionid") long solutionId,
                                        @RequestParam("employees") List<Long> employees,
                                        @RequestParam("personaldatas") List<Long> personalDatas,
                                        @RequestParam("accounts") List<Long> accounts) {
        userDao.removeUsers(solutionId, employees, personalDatas, accounts);
    }

    @RequestMapping(value = "/solution/employees/{id}", method = PATCH)
    public void updateEmployee(@PathVariable("id") long employeeId, @RequestBody User employee) {
        userDao.updateUser(employee);
    }

    @RequestMapping(value="/solution/employees", method = POST)
    public User addEmployee(@RequestBody Map<String, String> employee) {
        String password = passwordGenerator.generate(12);
        Long teamId = employee.get("teamid").isEmpty() ? null : Long.parseLong(employee.get("teamid"));
        String birthday = employee.get("birthday").isEmpty() ? null : employee.get("birthday");
        String phone = employee.get("phone").isEmpty() ? null : employee.get("phone");
        User user = userDao.addUser(Long.parseLong(employee.get("solutionid")), teamId,
                Long.parseLong(employee.get("positionid")), Double.parseDouble(employee.get("workingtime")),
                employee.get("firstname"), employee.get("lastname"), birthday, phone,
                employee.get("login"), password, employee.get("email"));
        notificator.sendInitialMessage(employee.get("login"), password, employee.get("email"));
        return user;
    }
}
