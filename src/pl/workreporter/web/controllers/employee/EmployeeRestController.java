package pl.workreporter.web.controllers.employee;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
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

    @RequestMapping(value = "/solution/employees", method = GET)
    public @ResponseBody
    List<User> getAllEmployees(@RequestParam("id") long solutionId) {
        List<User> result = userDao.getAllUsersInSolution(solutionId);
        return result;
    }

    @RequestMapping(value = "/solution/employees/{id}", method = DELETE)
    public void removeEmployee(@RequestParam("solutionid") long solutionId, @PathVariable("id") long employeeId) {
        userDao.removeUser(solutionId, employeeId);
    }

    @RequestMapping(value = "/solution/employees", method = DELETE)
    public void removeSelectedEmployees(@RequestParam("solutionid") long solutionId, @RequestParam("employees") List<Long> employees) {
        userDao.removeUsers(solutionId, employees);
    }

    @RequestMapping(value = "/solution/employees/{id}", method = PATCH)
    public void updateEmployee(@PathVariable("id") long employeeId, @RequestBody User employee) {
        userDao.updateUser(employee);
    }

    @RequestMapping(value="/solution/employees", method = POST)
    public User addEmployee(@RequestBody Map<String, String> employee) {
        String password = passwordGenerator.generate(12);
        System.out.println(password);
        return userDao.addUser(Long.parseLong(employee.get("solutionid")), Long.parseLong(employee.get("teamid")),
                Long.parseLong(employee.get("positionid")), Double.parseDouble(employee.get("workingtime")),
                employee.get("firstname"), employee.get("lastname"), employee.get("birthday"), employee.get("phone"),
                employee.get("login"), password, employee.get("email"));
    }
}
